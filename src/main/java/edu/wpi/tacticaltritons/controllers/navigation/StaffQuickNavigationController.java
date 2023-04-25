package edu.wpi.tacticaltritons.controllers.navigation;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

public class StaffQuickNavigationController {
    @FXML private MenuButton signageMenuButton;
    @FXML private MenuButton hospitalMapMenuButton;
    @FXML private MenuButton serviceRequestMenuButton;

    @FXML
    private void initialize(){
        signageMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(false, QuickNavigationMenuButtons.QuickNavigationMenu.SIGNAGE));
        signageMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        hospitalMapMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(false, QuickNavigationMenuButtons.QuickNavigationMenu.HOSPITAL_MAP));
        hospitalMapMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        serviceRequestMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(false, QuickNavigationMenuButtons.QuickNavigationMenu.SERVICE_REQUEST));
        serviceRequestMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());
    }
}
