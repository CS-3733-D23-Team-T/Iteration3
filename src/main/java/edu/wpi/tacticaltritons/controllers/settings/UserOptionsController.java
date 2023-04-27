package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserOptionsController {
    @FXML private Text userHeader;
    @FXML private MFXButton editUserButton;
    @FXML private MFXButton changeEmailButton;
    @FXML private MFXButton changePasswordButton;
    @FXML private MFXButton twoFactorButton;
    @FXML private MFXButton createAccountButton;
    @FXML private FlowPane userContent;
    @FXML private FlowPane userBottomPane;

    @FXML
    private void initialize() throws IOException {
        userHeader.setText(GoogleTranslate.translate("User Options"));
        editUserButton.setText(GoogleTranslate.translate("Edit User Info"));
        changeEmailButton.setText(GoogleTranslate.translate("Change Email"));
        changePasswordButton.setText(GoogleTranslate.translate("Change Password"));
        twoFactorButton.setText(GoogleTranslate.translate("Two-Factor Auth"));
        createAccountButton.setText(GoogleTranslate.translate("Create Account"));


        editUserButton.setOnAction(event -> SettingsNavigation.navigate(Screen.EDIT_USER));
        changeEmailButton.setOnAction(event -> SettingsNavigation.navigate(Screen.CHANGE_EMAIL));
        changePasswordButton.setOnAction(event -> SettingsNavigation.navigate(Screen.CHANGE_PASSWORD));
        twoFactorButton.setOnAction(event -> SettingsNavigation.navigate(Screen.TWO_FACTOR_AUTH));

        if(UserSessionToken.getUser() != null && !UserSessionToken.getUser().isAdmin()){
            userContent.getChildren().remove(createAccountButton);
        }
        createAccountButton.setOnAction(event -> SettingsNavigation.navigate(Screen.CREATE_ACCOUNT));
    }
}
