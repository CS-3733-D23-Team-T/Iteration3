package edu.wpi.tacticaltritons.controllers.settings;

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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class EditUserController {
    @FXML private MFXTextField usernameField;
    @FXML private Text usernameValidator;
    @FXML private MFXTextField firstNameField;
    @FXML private Text firstNameValidator;
    @FXML private MFXTextField lastNameField;
    @FXML private Text lastNameValidator;
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton confirmEditButton;
    private String currentUsername;
    private Session currentSession;

    @FXML
    private void initialize() throws SQLException {
        BooleanProperty validUsername = new SimpleBooleanProperty(true);
        this.usernameField.setText(UserSessionToken.getUser().getUsername());
        currentUsername = UserSessionToken.getUser().getUsername();
        currentSession = UserSessionToken.getUser();
        this.usernameField.textProperty().addListener(Validator.generateValidatorListener(validUsername, "[0-9A-Za-z]{3,32}",
                this.usernameValidator.getText(), this.usernameValidator));

        BooleanProperty validFirstName = new SimpleBooleanProperty(true);
        this.firstNameField.setText(UserSessionToken.getUser().getFirstname());
        this.firstNameField.textProperty().addListener(Validator.generateValidatorListener(validFirstName, "[A-Za-z]{1,50}",
                this.firstNameValidator.getText(), this.firstNameValidator));

        BooleanProperty validLastName = new SimpleBooleanProperty(true);
        this.lastNameField.setText(UserSessionToken.getUser().getLastname());
        this.lastNameField.textProperty().addListener(Validator.generateValidatorListener(validLastName, "[A-Za-z]{1,50}",
                this.lastNameValidator.getText(), this.lastNameValidator));

        validUsername.addListener(Validator.generateFormListener(this.confirmEditButton,
                validUsername,
                validFirstName,
                validLastName));

        validFirstName.addListener(Validator.generateFormListener(this.confirmEditButton,
                validUsername,
                validFirstName,
                validLastName));

        validLastName.addListener(Validator.generateFormListener(this.confirmEditButton,
                validUsername,
                validFirstName,
                validLastName));

        this.confirmEditButton.setOnAction(event -> {
            Session newSession = UserSessionToken.getUser();
            newSession.setUsername(usernameField.getText());
            newSession.setFirstname(firstNameField.getText());
            newSession.setLastname(lastNameField.getText());

            try{
                Login newLogin = DAOFacade.getLogin(currentUsername);
                DAOFacade.deleteLogin(newLogin);
                DAOFacade.deleteSession(DAOFacade.getSession(currentUsername));
                UserSessionToken.setUser(newSession);
                newLogin.setUsername(usernameField.getText());
                newLogin.setFirstName(firstNameField.getText());
                newLogin.setLastName(lastNameField.getText());

                //TODO figure out how to update the tables as well
                DAOFacade.addLogin(newLogin);
                DAOFacade.addSession(UserSessionToken.getUser());
                SettingsNavigation.navigate(Screen.USER_OPTIONS);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.cancelButton.setOnAction(event -> SettingsNavigation.navigate(Screen.USER_OPTIONS));
    }
}
