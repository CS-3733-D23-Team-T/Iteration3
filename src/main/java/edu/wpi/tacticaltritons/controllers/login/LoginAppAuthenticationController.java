package edu.wpi.tacticaltritons.controllers.login;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.ConfirmApp;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.auth.Validator;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoginAppAuthenticationController {
    @FXML private MFXTextField confirmationCodeField;
    @FXML private Text confirmationCodeValidator;
    @FXML private MFXButton confirmCodeButton;
    @FXML private MFXButton cancelButton;

    @FXML
    private void initialize() throws SQLException {
        Login login = DAOFacade.getLogin(LoginNavigation.retrievePacket());

        this.cancelButton.setOnAction(event ->
        {
            StackPane stackPane = (StackPane) App.getRootPane().getCenter();
            stackPane.getChildren().remove(2, stackPane.getChildren().size());
            Navigation.navigate(Screen.LOGIN);
        });

        BooleanProperty validConfirmationCode = new SimpleBooleanProperty(false);
        this.confirmationCodeField.textProperty().addListener(Validator.generateRestrictiveValidatorListener(
                validConfirmationCode, "[\\d]*", 6, confirmationCodeField));

        validConfirmationCode.addListener(Validator.generateFormListener(confirmCodeButton,
                validConfirmationCode));

        this.confirmCodeButton.setOnAction(event -> {
            if(ConfirmApp.APP_CODE.get().replace(" ", "").equals(confirmationCodeField.getText())){
                login.setLastLogin(LocalDateTime.now());
                try {
                    DAOFacade.updateLogin(login);
                    UserSessionToken.registerToken(login);
                    Navigation.navigate(Screen.HOME);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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
        });

        //usability
        this.confirmationCodeField.requestFocus();
        this.confirmationCodeField.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                confirmCodeButton.fire();
            }
            else if(event.getCode() == KeyCode.ESCAPE){
                cancelButton.fire();
            }
        });
    }
}
