package edu.wpi.tacticaltritons.controllers.navigation;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

public class LoginQuickNavigationController {
    @FXML private MenuButton hospitalMapMenuButton;
    @FXML private MenuButton signageMenuButton;

    @FXML
    private void initialize(){
        hospitalMapMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateServiceRequestItems(false, QuickNavigationMenuButtons.QuickNavigationMenu.HOSPITAL_MAP));
        hospitalMapMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        signageMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateServiceRequestItems(false, QuickNavigationMenuButtons.QuickNavigationMenu.SIGNAGE));
        signageMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());
    }
}
