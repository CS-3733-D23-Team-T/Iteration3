package edu.wpi.tacticaltritons.controllers.navigation;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;

public class LoginQuickNavigationController {
    @FXML private MenuButton hospitalMapMenuButton;
    @FXML private MenuButton signageMenuButton;
    @FXML private MenuButton loginButton;

    @FXML
    private void initialize(){
        hospitalMapMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(false, QuickNavigationMenuButtons.QuickNavigationMenu.HOSPITAL_MAP));
        hospitalMapMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        signageMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(false, QuickNavigationMenuButtons.QuickNavigationMenu.SIGNAGE));
        signageMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        loginButton.addEventHandler(EventType.ROOT, event -> {
            if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
                Navigation.navigate(Screen.LOGIN);
            }
        });
    }
}
