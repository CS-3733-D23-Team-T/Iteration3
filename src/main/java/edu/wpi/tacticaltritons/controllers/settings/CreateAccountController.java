package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.auth.Account;
import edu.wpi.tacticaltritons.auth.PasswordGenerator;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class CreateAccountController {
    @FXML private MFXTextField usernameField;
    @FXML private Text usernameValidator;
    @FXML private MFXTextField emailField;
    @FXML private Text emailValidator;
    @FXML private MFXTextField firstNameField;
    @FXML private Text firstNameValidator;
    @FXML private MFXTextField lastNameField;
    @FXML private Text lastNameValidator;
    @FXML private MFXPasswordField passwordField;
    @FXML private MFXButton generatePasswordButton;
    @FXML private Text passwordValidator;
    @FXML private Text passwordStrengthLabel;
    @FXML private ProgressBar passwordStrengthBar;
    @FXML private MFXPasswordField confirmPasswordField;
    @FXML private Text confirmPasswordValidator;
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton createAccountButton;

    private final boolean[] passwordChecks = {false, false, false, false, false, false, false, false};

    @FXML
    private void initialize() {
        BooleanProperty validUsername = new SimpleBooleanProperty(false);
        this.usernameField.textProperty().addListener(Validator.generateValidatorListener(validUsername,"[0-9A-Za-z]{3,32}",
                usernameValidator.getText(), usernameValidator));

        BooleanProperty validEmail = new SimpleBooleanProperty(false);
        this.emailField.textProperty().addListener(Validator.generateValidatorListener(validEmail, EmailValidator.getInstance(),
                emailValidator.getText(), emailValidator));

        BooleanProperty validFirstName = new SimpleBooleanProperty(false);
        this.firstNameField.textProperty().addListener(Validator.generateValidatorListener(validFirstName, "[A-Za-z]{1,50}",
                firstNameValidator.getText(), firstNameValidator));

        BooleanProperty validLastName = new SimpleBooleanProperty(false);
        this.lastNameField.textProperty().addListener(Validator.generateValidatorListener(validLastName, "[A-Za-z]{1,50}",
                lastNameValidator.getText(), lastNameValidator));

        BooleanProperty validPassword = new SimpleBooleanProperty(false);
        BooleanProperty validConfirmPassword = new SimpleBooleanProperty(false);
        SimpleObjectProperty<Validator.PasswordStrength> passwordStrengthProperty = new SimpleObjectProperty<>(Validator.PasswordStrength.Weak);
        this.passwordField.textProperty().addListener((obs, o, n) -> {
            passwordValidator.setText("Invalid Password");
            confirmPasswordValidator.setText("Passwords Don't Match");
            if(!(n == null || n.length() == 0)) {
                if(!passwordStrengthBar.isVisible()){
                    passwordStrengthBar.setVisible(true);
                    passwordStrengthLabel.setVisible(true);
                }
                if (Pattern.matches("^.*[a-z].*$", n) && !passwordChecks[0])
                    passwordChecks[0] = true;
                else if (!Pattern.matches("^.*[a-z].*$", n) && passwordChecks[0])
                    passwordChecks[0] = false;
                if (Pattern.matches("^.*[A-Z].*$", n) && !passwordChecks[1])
                    passwordChecks[1] = true;
                else if (!Pattern.matches("^.*[A-Z].*$", n) && passwordChecks[1])
                    passwordChecks[1] = false;
                if (Pattern.matches("^.*\\d.*$", n) && !passwordChecks[2])
                    passwordChecks[2] = true;
                else if (!Pattern.matches("^.*\\d.*$", n) && passwordChecks[2])
                    passwordChecks[2] = false;
                if (Pattern.matches("^.*[!@#$^%?&*].*$", n) && !passwordChecks[3])
                    passwordChecks[3] = true;
                else if (!Pattern.matches("^.*[!@#$^%?&*].*$", n) && passwordChecks[3])
                    passwordChecks[3] = false;
                passwordChecks[4] = Validator.containsDuplicate(n);
                passwordChecks[5] = Validator.containsSequence(n);
                passwordChecks[6] = Pattern.matches("^.*password.*$", n);
                passwordStrengthProperty.set(Validator.computePasswordStrength(n.length(), passwordChecks));
                if(!Pattern.matches("[!@#$^%?&*\\dA-Za-z]{3,64}", n) ||
                        passwordStrengthProperty.get() == Validator.PasswordStrength.Weak){

                    if(!passwordValidator.isVisible()) passwordValidator.setVisible(true);
                    validPassword.set(false);
                }
                else{
                    if(passwordValidator.isVisible()) passwordValidator.setVisible(false);
                    validPassword.set(true);
                }
            }
            else {
                if(!passwordValidator.isVisible()) passwordValidator.setVisible(true);
                if(passwordStrengthBar.isVisible()) passwordStrengthBar.setVisible(false);
                if(passwordStrengthLabel.isVisible()) passwordStrengthLabel.setVisible(false);
                validPassword.set(false);
            }
            if(confirmPasswordField.getText() != null && n != null && n.equals(confirmPasswordField.getText())){
                validConfirmPassword.set(true);
                if(confirmPasswordValidator.isVisible()) confirmPasswordValidator.setVisible(false);
            }
            else if(confirmPasswordField.getText() != null && n != null && !n.equals(confirmPasswordField.getText())){
                validConfirmPassword.set(false);
                if(!confirmPasswordValidator.isVisible()) confirmPasswordValidator.setVisible(true);
            }
        });


        this.passwordStrengthLabel.textProperty().bind(passwordStrengthProperty.asString());
        passwordStrengthProperty.addListener(Validator.generatePasswordStrengthListener(
                passwordStrengthBar,
                passwordStrengthLabel
        ));

        this.confirmPasswordField.textProperty().addListener((obs, o, n) -> {
            confirmPasswordValidator.setText("Passwords Don't Match");
            if (n != null && passwordField.getText() != null && n.equals(passwordField.getText())) {
                validConfirmPassword.set(true);
                if(confirmPasswordValidator.isVisible()) confirmPasswordValidator.setVisible(false);
            }
            else if(n != null && passwordField.getText() != null && !n.equals(passwordField.getText())){
                validConfirmPassword.set(false);
                if(!confirmPasswordValidator.isVisible()) confirmPasswordValidator.setVisible(true);
            }
        });

        this.generatePasswordButton.setOnAction(PasswordGenerator.generatePasswordEvent(
                passwordField,
                confirmPasswordField
        ));

        this.cancelButton.setOnAction(event -> SettingsNavigation.navigate(Screen.USER_OPTIONS));
        this.createAccountButton.setOnAction(event -> {
            try {
                int retValue = Account.createUser(
                        this.usernameField.getText(),
                        this.passwordField.getText(),
                        this.emailField.getText(),
                        this.passwordField.getText(),
                        this.lastNameField.getText(),
                        false);
                if (retValue == 1) {
//TODO send a notification showing success
                    SettingsNavigation.navigate(Screen.USER_OPTIONS);
                }
                else if(retValue == 2){
                    this.emailValidator.setText("An Account Already Exists for this Email");
                    this.emailValidator.setVisible(true);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        ChangeListener<? super Boolean> formListener = Validator.generateFormListener(createAccountButton,
                validUsername,
                validEmail,
                validFirstName,
                validLastName,
                validPassword,
                validConfirmPassword);
        validUsername.addListener(formListener);
        validEmail.addListener(formListener);
        validFirstName.addListener(formListener);
        validLastName.addListener(formListener);
        validPassword.addListener(formListener);
        validConfirmPassword.addListener(formListener);


//usability
        this.usernameField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                this.createAccountButton.fire();
            }
            else if(event.getCode().equals(KeyCode.ESCAPE)){
                this.cancelButton.fire();
            }
        });
        this.emailField.setOnKeyPressed(usernameField.getOnKeyPressed());
        this.firstNameField.setOnKeyPressed(usernameField.getOnKeyPressed());
        this.lastNameField.setOnKeyPressed(usernameField.getOnKeyPressed());
        this.passwordField.setOnKeyPressed(usernameField.getOnKeyPressed());
        this.confirmPasswordField.setOnKeyPressed(usernameField.getOnKeyPressed());
        this.usernameField.requestFocus();
    }


}
