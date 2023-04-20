package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.auth.Account;
import edu.wpi.tacticaltritons.auth.PasswordGenerator;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class ChangePasswordController {
    @FXML private MFXPasswordField oldPasswordField;
    @FXML private Text oldPasswordValidator;
    @FXML private MFXPasswordField newPasswordField;
    @FXML private MFXButton generatePasswordButton;
    @FXML private Text newPasswordValidator;
    @FXML private Text newPasswordStrengthLabel;
    @FXML private ProgressBar newPasswordStrengthBar;
    @FXML private MFXPasswordField confirmNewPasswordField;
    @FXML private Text confirmNewPasswordValidator;
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton updatePasswordButton;

    private final boolean[] passwordChecks = {false, false, false, false, false, false, false, false};
    @FXML
    private void initialize(){
        if(UserSessionToken.getUser() == null){
            Navigation.navigate(Screen.LOGIN);
        }

        BooleanProperty validOldPassword = new SimpleBooleanProperty(false);
        this.oldPasswordField.textProperty().addListener(Validator.generateValidatorListener(validOldPassword, "[!@#$^%?&*\\dA-Za-z]{3,64}",
                this.oldPasswordValidator.getText(), this.oldPasswordValidator));

        BooleanProperty validNewPassword = new SimpleBooleanProperty(false);
        BooleanProperty validConfirmNewPassword = new SimpleBooleanProperty(false);
        SimpleObjectProperty<Validator.PasswordStrength> passwordStrengthProperty = new SimpleObjectProperty<>(Validator.PasswordStrength.Weak);
        this.newPasswordField.textProperty().addListener((obs, o, n) -> {
            if(!(n == null || n.length() == 0)) {
                if(oldPasswordField.getText() != null && n.equals(oldPasswordField.getText())){
                    validOldPassword.set(false);
                    oldPasswordValidator.setText("Old and New Password Match");
                    oldPasswordValidator.setVisible(true);
                }
                else if(oldPasswordField.getText() != null && !n.equals(oldPasswordField.getText())){
                    validOldPassword.set(true);
                    oldPasswordValidator.setVisible(false);
                    oldPasswordValidator.setText("Invalid Password");
                }

                if(!this.newPasswordStrengthBar.isVisible()){
                    this.newPasswordStrengthBar.setVisible(true);
                    this.newPasswordStrengthLabel.setVisible(true);
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
                    this.newPasswordValidator.setVisible(true);
                    validNewPassword.set(false);
                }
                else{
                    this.newPasswordValidator.setVisible(false);
                    validNewPassword.set(true);
                }
            }
            else {
                this.newPasswordValidator.setVisible(true);
                this.newPasswordStrengthBar.setVisible(false);
                this.newPasswordStrengthLabel.setVisible(false);
                validNewPassword.set(false);
            }
            if(confirmNewPasswordField.getText() != null && n != null && n.equals(confirmNewPasswordField.getText())){
                validConfirmNewPassword.set(true);
                confirmNewPasswordValidator.setVisible(false);
            }
            else if(confirmNewPasswordField.getText() != null && n != null && !n.equals(confirmNewPasswordField.getText())){
                validConfirmNewPassword.set(false);
                confirmNewPasswordValidator.setVisible(true);
            }
        });


        this.newPasswordStrengthLabel.textProperty().bind(passwordStrengthProperty.asString());
        passwordStrengthProperty.addListener((obs, o, n) -> {
            switch(n){
                case Weak -> {
                    this.newPasswordStrengthBar.setStyle("-fx-accent: red");
                    this.newPasswordStrengthLabel.setFill(Color.rgb(255,0,0));
                    this.newPasswordStrengthBar.setProgress(.1);
                }
                case Basic -> {
                    this.newPasswordStrengthBar.setStyle("-fx-accent: orange");
                    this.newPasswordStrengthLabel.setFill(Color.rgb(255,165,0));
                    this.newPasswordStrengthBar.setProgress(.25);
                }
                case Good -> {
                    this.newPasswordStrengthBar.setStyle("-fx-accent: green");
                    this.newPasswordStrengthLabel.setFill(Color.rgb(0,128,0));
                    this.newPasswordStrengthBar.setProgress(.6);
                }
                case Strong -> {
                    this.newPasswordStrengthBar.setStyle("-fx-accent: purple");
                    this.newPasswordStrengthLabel.setFill(Color.rgb(128,0,128));
                    this.newPasswordStrengthBar.setProgress(1);
                }
            }
        });

        this.confirmNewPasswordField.textProperty().addListener((obs, o, n) -> {
            if (n != null && this.newPasswordField.getText() != null && n.equals(this.newPasswordField.getText())) {
                validConfirmNewPassword.set(true);
                confirmNewPasswordValidator.setVisible(false);
            }
            else if(n != null && this.newPasswordField.getText() != null && !n.equals(this.newPasswordField.getText())){
                validConfirmNewPassword.set(false);
                confirmNewPasswordField.setVisible(true);
            }
        });

        this.generatePasswordButton.setOnAction(event -> {
            String generatedPassword = PasswordGenerator.generatePassword();
            this.newPasswordField.setText(generatedPassword);
            this.newPasswordField.setShowPassword(true);
            this.confirmNewPasswordField.setText(generatedPassword);
            this.confirmNewPasswordField.setShowPassword(true);
        });

        this.cancelButton.setOnAction(event -> SettingsNavigation.navigate(Screen.USER_OPTIONS));

        this.updatePasswordButton.setOnAction(event -> {
            int ret = Account.updatePassword(newPasswordField.getText(), oldPasswordField.getText());
            if(ret == 1){
                SettingsNavigation.navigate(Screen.USER_OPTIONS);
            }
            else if(ret == 2){
                oldPasswordValidator.setText("Old Password Does Not Match");
                oldPasswordValidator.setVisible(true);
            }
        });

        validOldPassword.addListener(Validator.generateFormListener(updatePasswordButton,
                validOldPassword,
                validNewPassword,
                validConfirmNewPassword));

        validNewPassword.addListener(Validator.generateFormListener(updatePasswordButton,
                validOldPassword,
                validNewPassword,
                validConfirmNewPassword));

        validConfirmNewPassword.addListener(Validator.generateFormListener(updatePasswordButton,
                validOldPassword,
                validNewPassword,
                validConfirmNewPassword));
    }
}
