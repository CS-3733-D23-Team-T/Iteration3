package edu.wpi.tacticaltritons.controllers.login;

import edu.wpi.tacticaltritons.auth.Account;
import edu.wpi.tacticaltritons.auth.ConfirmEmail;
import edu.wpi.tacticaltritons.auth.PasswordGenerator;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.navigation.LoginNavigation;
import edu.wpi.tacticaltritons.navigation.Screen;
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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ResetPasswordController {
    @FXML private Text resetTitleText;
    @FXML private Text emailText;
    @FXML private MFXTextField emailField;
    @FXML private Text emailValidator;
    @FXML private MFXTextField confirmationCodeField;
    @FXML private Text confirmationCodeValidator;
    @FXML private MFXPasswordField newPasswordField;
    @FXML private MFXButton generatePasswordButton;
    @FXML private Text passwordValidator;
    @FXML private Text passwordStrengthLabel;
    @FXML private ProgressBar passwordStrengthBar;
    @FXML private MFXPasswordField confirmPasswordField;
    @FXML private Text confirmPasswordValidator;
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton confirmButton;

    //step 0 -> send email
    //step 2 -> enter in confirmation code
    //step 3 -> reset password
    private int currentStep = 0;
    private final boolean[] passwordChecks = {false, false, false, false, false, false, false, false};
    @FXML
    private void initialize(){
        String[] code = new String[1];

        List<String> emails = new ArrayList<>();
        new Thread(() -> {
            try {
                emails.addAll(DAOFacade.getAllLogins().stream().map(Login::getEmail).toList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();

        emailField.requestFocus();

        BooleanProperty validEmail = new SimpleBooleanProperty(false);
        this.emailField.textProperty().addListener(Validator.generateValidatorListener(validEmail,
                EmailValidator.getInstance(), emailValidator.getText(), emailValidator));


        ChangeListener<? super Boolean> stepOneFormListener = Validator.generateFormListener(confirmButton,
                validEmail);
        validEmail.addListener(stepOneFormListener);

        BooleanProperty validConfirmationCode = new SimpleBooleanProperty(false);
        this.confirmationCodeField.textProperty().addListener(Validator.generateRestrictiveValidatorListener(validConfirmationCode,
                "[\\d]*",6, confirmationCodeField));

        ChangeListener<? super Boolean> stepTwoFormListener = Validator.generateFormListener(confirmButton,
                validConfirmationCode);

        BooleanProperty validPassword = new SimpleBooleanProperty(false);
        BooleanProperty validConfirmPassword = new SimpleBooleanProperty(false);
        SimpleObjectProperty<Validator.PasswordStrength> passwordStrengthProperty = new SimpleObjectProperty<>(Validator.PasswordStrength.Weak);
        this.newPasswordField.textProperty().addListener((obs, o, n) -> {
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
            if (n != null && newPasswordField.getText() != null && n.equals(newPasswordField.getText())) {
                validConfirmPassword.set(true);
                if(confirmPasswordValidator.isVisible()) confirmPasswordValidator.setVisible(false);
            }
            else if(n != null && newPasswordField.getText() != null && !n.equals(newPasswordField.getText())){
                validConfirmPassword.set(false);
                if(!confirmPasswordValidator.isVisible()) confirmPasswordValidator.setVisible(true);
            }
        });

        this.generatePasswordButton.setOnAction(PasswordGenerator.generatePasswordEvent(
                newPasswordField,
                confirmPasswordField
        ));

        ChangeListener<? super Boolean> stepThreeFormListener = Validator.generateFormListener(confirmButton,
                validPassword,
                validConfirmPassword);

        this.confirmButton.setOnAction(event -> {
            if(currentStep == 0){
                if(emails.contains(emailField.getText())){
                    validEmail.removeListener(stepOneFormListener);
                    validConfirmationCode.addListener(stepTwoFormListener);

                    code[0] = ConfirmEmail.sendRestEmail(emailField.getText());
                    emailText.setText(emailField.getText());

                    setStage(++currentStep);
                    confirmationCodeField.requestFocus();
                    this.confirmButton.setDisable(true);
                }
                else{
                    emailValidator.setText("No Account Exists With That Email");
                    emailValidator.setVisible(true);
                }
            }
            else if(currentStep == 1){
                if(code[0].equals(this.confirmationCodeField.getText())){
                    validConfirmationCode.removeListener(stepTwoFormListener);
                    validPassword.addListener(stepThreeFormListener);
                    validConfirmPassword.addListener(stepThreeFormListener);
                    setStage(++currentStep);
                    newPasswordField.requestFocus();
                    this.confirmButton.setText("Reset");
                    this.confirmButton.setDisable(true);
                }
                else{
                    confirmationCodeValidator.setText("Incorrect Code");
                    confirmationCodeValidator.setVisible(true);
                }
            }
            else if(currentStep == 2){
                int returnValue = Account.resetPassword(newPasswordField.getText(), emailField.getText());
                if(returnValue == 1) LoginNavigation.navigate(Screen.LOGIN_CONTAINER);
                else if(returnValue == 2){
                    confirmPasswordValidator.setText("Old and New Password Match");
                    confirmPasswordValidator.setVisible(true);
                    validConfirmPassword.set(false);
                    validPassword.set(false);
                }
            }
        });

        this.cancelButton.setOnAction(event -> {
            if(currentStep == 0)
            {
                LoginNavigation.navigate(Screen.LOGIN);
            }
            else{
                if(currentStep == 1){
                    validConfirmationCode.removeListener(stepTwoFormListener);
                }
                else if(currentStep == 2){
                    validPassword.removeListener(stepThreeFormListener);
                    validConfirmPassword.removeListener(stepThreeFormListener);
                }
                currentStep = 0;
                setStage(currentStep);
                emailField.requestFocus();
            }
        });

        this.emailField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                confirmButton.fire();
            }
            else if(event.getCode() == KeyCode.ESCAPE){
                cancelButton.fire();
            }
        });

        this.confirmationCodeField.setOnKeyPressed(emailField.getOnKeyPressed());
        this.newPasswordField.setOnKeyPressed(emailField.getOnKeyPressed());
        this.confirmPasswordField.setOnKeyPressed(emailField.getOnKeyPressed());
    }
    private void setStage(int increment){
        if(increment == 0){
            resetTitleText.setText("Account Email");
            cancelButton.setText("Cancel");
            confirmButton.setText("Send");
            resetTitleText.setVisible(true);
            resetTitleText.setDisable(false);

            emailText.setVisible(false);
            emailText.setDisable(true);

            emailField.setVisible(true);
            emailField.setDisable(false);

            emailValidator.setVisible(false);
            emailValidator.setDisable(true);

            confirmationCodeField.setVisible(false);
            confirmationCodeField.setDisable(true);

            confirmationCodeValidator.setVisible(false);
            confirmationCodeValidator.setDisable(true);

            newPasswordField.setVisible(false);
            newPasswordField.setDisable(true);

            generatePasswordButton.setVisible(false);
            generatePasswordButton.setDisable(true);

            passwordValidator.setVisible(false);
            passwordValidator.setDisable(true);

            passwordStrengthLabel.setVisible(false);
            passwordStrengthLabel.setDisable(true);

            passwordStrengthBar.setVisible(false);
            passwordStrengthBar.setDisable(true);

            confirmPasswordField.setVisible(false);
            confirmPasswordField.setDisable(true);

            confirmPasswordValidator.setVisible(false);
            confirmPasswordValidator.setDisable(true);
        }
        else if(increment == 1){
            resetTitleText.setText("Sent Reset Code To");
            cancelButton.setText("Back");
            confirmButton.setText("Confirm");
            resetTitleText.setVisible(true);
            resetTitleText.setDisable(false);

            emailText.setVisible(true);
            emailText.setDisable(false);

            emailField.setVisible(true);
            emailField.setDisable(true);

            emailValidator.setVisible(false);
            emailValidator.setDisable(true);

            confirmationCodeField.setVisible(true);
            confirmationCodeField.setDisable(false);

            confirmationCodeValidator.setVisible(false);
            confirmationCodeValidator.setDisable(false);

            newPasswordField.setVisible(false);
            newPasswordField.setDisable(true);

            generatePasswordButton.setVisible(false);
            generatePasswordButton.setDisable(true);

            passwordValidator.setVisible(false);
            passwordValidator.setDisable(true);

            passwordStrengthLabel.setVisible(false);
            passwordStrengthLabel.setDisable(true);

            passwordStrengthBar.setVisible(false);
            passwordStrengthBar.setDisable(true);

            confirmPasswordField.setVisible(false);
            confirmPasswordField.setDisable(true);

            confirmPasswordValidator.setVisible(false);
            confirmPasswordValidator.setDisable(true);
        }
        else if(increment == 2){
            confirmButton.setText("Reset");
            cancelButton.setText("Back");
            resetTitleText.setVisible(true);
            resetTitleText.setDisable(false);

            emailText.setVisible(true);
            emailText.setDisable(false);

            emailField.setVisible(true);
            emailField.setDisable(true);

            emailValidator.setVisible(false);
            emailValidator.setDisable(true);

            confirmationCodeField.setVisible(true);
            confirmationCodeField.setDisable(true);

            confirmationCodeValidator.setVisible(false);
            confirmationCodeValidator.setDisable(true);

            newPasswordField.setVisible(true);
            newPasswordField.setDisable(false);

            generatePasswordButton.setVisible(true);
            generatePasswordButton.setDisable(false);

            passwordValidator.setVisible(false);
            passwordValidator.setDisable(false);

            passwordStrengthLabel.setVisible(false);
            passwordStrengthLabel.setDisable(false);

            passwordStrengthBar.setVisible(false);
            passwordStrengthBar.setDisable(false);

            confirmPasswordField.setVisible(true);
            confirmPasswordField.setDisable(false);

            confirmPasswordValidator.setVisible(false);
            confirmPasswordValidator.setDisable(false);
        }
    }
}
