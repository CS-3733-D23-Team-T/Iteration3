package edu.wpi.tacticaltritons.controllers.login;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.*;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.navigation.LoginNavigation;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML private MFXTextField usernameField;
    @FXML private Text usernameValidator;
    @FXML private MFXTextField passwordField;
    @FXML private Text passwordValidator;
    @FXML private MFXButton loginButton;
    @FXML private Text resetPasswordLink;

    @FXML
    private void initialize() {
        usernameField.setPromptText(GoogleTranslate.getString("username"));
        passwordField.setPromptText(GoogleTranslate.getString("password"));
        loginButton.setText(GoogleTranslate.getString("Login"));
        resetPasswordLink.setText(GoogleTranslate.getString("resetPassword"));
        usernameValidator.setText(GoogleTranslate.getString("usernameValidator"));
        passwordValidator.setText(GoogleTranslate.getString("passwordValidator"));

        BooleanProperty validUsername = new SimpleBooleanProperty(false);
        this.usernameField.textProperty().addListener(Validator.generateValidatorListener(validUsername, "[0-9A-Za-z]{3,32}",
                this.usernameValidator.getText(), this.usernameValidator));

        BooleanProperty validPassword = new SimpleBooleanProperty(false);
        this.passwordField.textProperty().addListener(Validator.generateValidatorListener(validPassword, "[!@#$^%?&*\\dA-Za-z]{3,64}",
                this.passwordValidator.getText(), this.passwordValidator));

        this.loginButton.setOnAction(event -> {
            int ret = Account.attemptLogin(usernameField.getText(), passwordField.getText());
            Login login;
            if (ret == 1) {
                try {
                    login = DAOFacade.getLogin(usernameField.getText());
                    UserSessionToken.registerToken(login);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                GoogleTranslate.setLanguage(Language.parseLanguage(login.getLanguage()).getLanguage());
                FXMLLoader navLoader = new FXMLLoader(App.class.getResource("views/NavigationBar.fxml"));
                FXMLLoader adminLoader = new FXMLLoader(App.class.getResource("views/navigation/AdminQuickNavigation.fxml"));
                FXMLLoader staffLoader = new FXMLLoader(App.class.getResource("views/navigation/StaffQuickNavigation.fxml"));
                try {
                    App.quickNavigationMenuButtons = new QuickNavigationMenuButtons();
                    App.setAdminQuickNavigation(adminLoader.load());
                    App.setStaffQuickNavigation(staffLoader.load());
                    App.setNavBar(navLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                App.getPrimaryStage().setScene(App.getRootPane().getScene());
                App.getPrimaryStage().show();
                Navigation.navigate(Screen.HOME);
            }
            else if(ret == -1){
                passwordValidator.setText("Incorrect Username or Password");
                passwordValidator.setVisible(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    passwordValidator.setVisible(false);
                }).start();
            }
            else if(ret == 2){
                LoginNavigation.attachPacket(usernameField.getText());
                try {
                    Login attemptedLogin = DAOFacade.getLogin(usernameField.getText());
                    if(attemptedLogin.getTwoFactorMethods()[0].equals(AuthenticationMethod.EMAIL.name())){
                        LoginNavigation.navigate(Screen.LOGIN_AUTHENTICATION);
                    }
                    else if(attemptedLogin.getTwoFactorMethods()[0].equals(AuthenticationMethod.APP.name())){
                        LoginNavigation.navigate(Screen.APP_LOGIN_AUTHENTICATION);
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        validUsername.addListener(Validator.generateFormListener(loginButton,
                validUsername,
                validPassword));

        validPassword.addListener(Validator.generateFormListener(loginButton,
                validUsername,
                validPassword));

        this.usernameField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                this.loginButton.fire();
            }
        });
        this.passwordField.setOnKeyPressed(this.usernameField.getOnKeyPressed());

        this.resetPasswordLink.addEventHandler(EventType.ROOT, event -> {
            if(event.getEventType() == MouseEvent.MOUSE_ENTERED){
                this.resetPasswordLink.setUnderline(true);
            }
            else if(event.getEventType() == MouseEvent.MOUSE_EXITED){
                this.resetPasswordLink.setUnderline(false);
            }
        });
        this.resetPasswordLink.setOnMouseClicked(event -> LoginNavigation.navigate(Screen.RESET_PASSWORD));
    }
}
