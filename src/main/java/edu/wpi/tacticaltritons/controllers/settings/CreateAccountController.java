package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.auth.Account;
import edu.wpi.tacticaltritons.auth.PasswordGenerator;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

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
      if(!(n == null || n.length() == 0)) {
        if(!this.passwordStrengthBar.isVisible()){
          this.passwordStrengthBar.setVisible(true);
          this.passwordStrengthLabel.setVisible(true);
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
          this.passwordValidator.setVisible(true);
          validPassword.set(false);
        }
        else{
          this.passwordValidator.setVisible(false);
          validPassword.set(true);
        }
      }
      else {
        this.passwordValidator.setVisible(true);
        this.passwordStrengthBar.setVisible(false);
        this.passwordStrengthLabel.setVisible(false);
        validPassword.set(false);
      }
      if(confirmPasswordField.getText() != null && n != null && n.equals(confirmPasswordField.getText())){
        validConfirmPassword.set(true);
        confirmPasswordValidator.setVisible(false);
      }
      else if(confirmPasswordField.getText() != null && n != null && !n.equals(confirmPasswordField.getText())){
        validConfirmPassword.set(false);
        confirmPasswordValidator.setVisible(true);
      }
    });


    this.passwordStrengthLabel.textProperty().bind(passwordStrengthProperty.asString());
    passwordStrengthProperty.addListener((obs, o, n) -> {
      switch(n){
        case Weak -> {
          this.passwordStrengthBar.setStyle("-fx-accent: red");
          this.passwordStrengthLabel.setFill(Color.rgb(255,0,0));
          this.passwordStrengthBar.setProgress(.1);
        }
        case Basic -> {
          this.passwordStrengthBar.setStyle("-fx-accent: orange");
          this.passwordStrengthLabel.setFill(Color.rgb(255,165,0));
          this.passwordStrengthBar.setProgress(.25);
        }
        case Good -> {
          this.passwordStrengthBar.setStyle("-fx-accent: green");
          this.passwordStrengthLabel.setFill(Color.rgb(0,128,0));
          this.passwordStrengthBar.setProgress(.6);
        }
        case Strong -> {
          this.passwordStrengthBar.setStyle("-fx-accent: purple");
          this.passwordStrengthLabel.setFill(Color.rgb(128,0,128));
          this.passwordStrengthBar.setProgress(1);
        }
      }
    });

    this.confirmPasswordField.textProperty().addListener((obs, o, n) -> {
      if (n != null && this.passwordField.getText() != null && n.equals(this.passwordField.getText())) {
        validConfirmPassword.set(true);
        confirmPasswordValidator.setVisible(false);
      }
      else if(n != null && this.passwordField.getText() != null && !n.equals(this.passwordField.getText())){
        validConfirmPassword.set(false);
        confirmPasswordValidator.setVisible(true);
      }
    });

    this.generatePasswordButton.setOnAction(event -> {
      String generatedPassword = PasswordGenerator.generatePassword();
      this.passwordField.setText(generatedPassword);
      this.passwordField.setShowPassword(true);
      this.confirmPasswordField.setText(generatedPassword);
      this.confirmPasswordField.setShowPassword(true);
    });

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

    validUsername.addListener(Validator.generateFormListener(createAccountButton,
            validUsername,
            validEmail,
            validFirstName,
            validLastName,
            validPassword,
            validConfirmPassword));
    validEmail.addListener(Validator.generateFormListener(createAccountButton,
            validUsername,
            validEmail,
            validFirstName,
            validLastName,
            validPassword,
            validConfirmPassword));
    validFirstName.addListener(Validator.generateFormListener(createAccountButton,
            validUsername,
            validEmail,
            validFirstName,
            validLastName,
            validPassword,
            validConfirmPassword));
    validLastName.addListener(Validator.generateFormListener(createAccountButton,
            validUsername,
            validEmail,
            validFirstName,
            validLastName,
            validPassword,
            validConfirmPassword));
    validPassword.addListener(Validator.generateFormListener(createAccountButton,
            validUsername,
            validEmail,
            validFirstName,
            validLastName,
            validPassword,
            validConfirmPassword));
    validConfirmPassword.addListener(Validator.generateFormListener(createAccountButton,
            validUsername,
            validEmail,
            validFirstName,
            validLastName,
            validPassword,
            validConfirmPassword));


    this.usernameField.setOnKeyPressed(event -> {
      if(event.getCode().equals(KeyCode.ENTER)){
        this.createAccountButton.fire();
      }
      else if(event.getCode().equals(KeyCode.ESCAPE)){
        this.cancelButton.fire();
      }
    });
    this.usernameField.requestFocus();
  }


}
