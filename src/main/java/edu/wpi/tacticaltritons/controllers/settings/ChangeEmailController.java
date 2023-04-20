package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.auth.ConfirmEmail;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.database.Session;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.FadeTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

public class ChangeEmailController {
    @FXML private Text oldEmail;
    @FXML private MFXTextField emailField;
    @FXML private Text emailValidator;
    @FXML private MFXTextField confirmEmailField;
    @FXML private Text confirmEmailValidator;
    @FXML private MFXTextField confirmationCodeField;
    @FXML private Text confirmationCodeValidator;
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton confirmEmailButton;

    private int stage = 1;
    private String code = null;
    @FXML
    private void initialize(){
        if(UserSessionToken.getUser() == null){
            Navigation.navigate(Screen.HOME);
        }
        this.oldEmail.setText(UserSessionToken.getUser().getEmail());

        BooleanProperty validEmail = new SimpleBooleanProperty(false);
        BooleanProperty validConfirmEmail = new SimpleBooleanProperty(false);
        this.emailField.textProperty().addListener((obs, o, n) -> {
            if(n != null){
                if(EmailValidator.getInstance().isValid(n)){
                    if(n.equals(oldEmail.getText())){
                        validEmail.set(false);
                        if(!emailValidator.isVisible()){
                            emailValidator.setText("Same Email as Previous");
                            emailValidator.setVisible(true);
                        }
                    }
                    else{
                        validEmail.set(true);
                        if(emailValidator.isVisible()){
                            emailValidator.setVisible(false);
                        }
                    }
                }
                else if(!EmailValidator.getInstance().isValid(n)){
                    validEmail.set(false);
                    if(!emailValidator.isVisible()){
                        emailValidator.setText("Invalid Email");
                        emailValidator.setVisible(true);
                    }
                }

                if(confirmEmailField.getText() != null && n.equals(confirmEmailField.getText())){
                    validConfirmEmail.set(true);
                    if(confirmEmailValidator.isVisible()){
                        confirmEmailValidator.setVisible(false);
                    }
                }
                else if(confirmEmailField.getText() != null && !n.equals(confirmEmailField.getText())){
                    validConfirmEmail.set(false);
                    if(!confirmEmailValidator.isVisible()){
                        confirmEmailValidator.setVisible(true);
                    }
                }
            }
        });


        this.confirmEmailField.textProperty().addListener((obs, o, n) -> {
            if(n != null){
                if(emailField.getText() != null && n.equals(emailField.getText())){
                    validConfirmEmail.set(true);
                    if(confirmEmailValidator.isVisible()){
                        confirmEmailValidator.setVisible(false);
                    }
                }
                else if(emailField.getText() != null && !n.equals(emailField.getText())){
                    validConfirmEmail.set(false);
                    if(confirmEmailValidator.isVisible()){
                        confirmEmailValidator.setVisible(true);
                    }
                }
            }
        });

        BooleanProperty validConfirmationCode = new SimpleBooleanProperty(false);
        this.confirmationCodeField.textProperty().addListener(Validator.generateRestrictiveValidatorListener(
                validConfirmationCode, "[\\d]*", 6, this.confirmationCodeField
        ));

        ChangeListener<? super Boolean> confirmationCodeListener = Validator.generateFormListener(confirmEmailButton, validConfirmationCode);
        validEmail.addListener(Validator.generateFormListener(confirmEmailButton,
                validEmail,
                validConfirmEmail));

        validConfirmEmail.addListener(Validator.generateFormListener(confirmEmailButton,
                validEmail,
                validConfirmEmail));



        this.cancelButton.setOnAction(event -> {
            if(stage == 1) {
                SettingsNavigation.navigate(Screen.USER_OPTIONS);
            }
            else if(stage == 2){
                emailField.setDisable(false);
                confirmEmailField.setDisable(false);

                confirmationCodeField.setVisible(false);
                code = null;

                confirmEmailButton.setText("Confirm");

                validConfirmationCode.removeListener(confirmationCodeListener);

                stage--;
            }
        });

        this.confirmEmailButton.setOnAction(event -> {
            if(stage == 1){
                emailField.setDisable(true);
                confirmEmailField.setDisable(true);

                confirmationCodeField.setVisible(true);
                code = ConfirmEmail.sendConfirmationEmail(emailField.getText());

                confirmEmailButton.setText("Update");

                validConfirmationCode.addListener(confirmationCodeListener);

                stage++;
            }
            else if(stage == 2) {
                if (code.equals(confirmationCodeField.getText())) {
                    Session newSession = UserSessionToken.getUser();
                    newSession.setEmail(emailField.getText());

                    try {
                        Login newLogin = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());
                        newLogin.setEmail(emailField.getText());

                        DAOFacade.deleteSession(UserSessionToken.getUser());
                        DAOFacade.addSession(newSession);
                        UserSessionToken.setUser(newSession);

                        DAOFacade.deleteLogin(newLogin);
                        DAOFacade.addLogin(newLogin);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    SettingsNavigation.navigate(Screen.USER_OPTIONS);
                }
                else{
                    confirmationCodeValidator.setVisible(true);
                    new Thread(() -> {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        confirmationCodeValidator.setVisible(false);
                    }).start();
                }
            }
        });
    }
}
