package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;

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
    private void initialize(){
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
