package edu.wpi.tacticaltritons.controllers.login;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.*;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.navigation.LoginNavigation;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class LoginController {
    @FXML private MFXTextField usernameField;
    @FXML private Text usernameValidator;
    @FXML private MFXTextField passwordField;
    @FXML private Text passwordValidator;
    @FXML private MFXButton loginButton;

    @FXML
    private void initialize() {
        BooleanProperty validUsername = new SimpleBooleanProperty(false);
        this.usernameField.textProperty().addListener(Validator.generateValidatorListener(validUsername, "[0-9A-Za-z]{3,32}",
                this.usernameValidator.getText(), this.usernameValidator));

        BooleanProperty validPassword = new SimpleBooleanProperty(false);
        this.passwordField.textProperty().addListener(Validator.generateValidatorListener(validPassword, "[!@#$^%?&*\\dA-Za-z]{3,64}",
                this.passwordValidator.getText(), this.passwordValidator));

        this.loginButton.setOnAction(event -> {
            int ret = Account.attemptLogin(usernameField.getText(), passwordField.getText());

            if (ret == 1) {
                try {
                    UserSessionToken.registerToken(DAOFacade.getLogin(usernameField.getText()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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
    }
}
