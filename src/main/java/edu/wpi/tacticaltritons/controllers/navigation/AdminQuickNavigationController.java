package edu.wpi.tacticaltritons.controllers.navigation;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

public class AdminQuickNavigationController {
    @FXML private MenuButton serviceRequestMenuButton;
    @FXML private MenuButton hospitalMapMenuButton;
    @FXML private MenuButton signageMenuButton;
    @FXML private MenuButton databaseMenuButton;

    @FXML
    private void initialize(){
        serviceRequestMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateServiceRequestItems(true, QuickNavigationMenuButtons.QuickNavigationMenu.SERVICE_REQUEST));
        serviceRequestMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        hospitalMapMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateServiceRequestItems(true, QuickNavigationMenuButtons.QuickNavigationMenu.HOSPITAL_MAP));
        hospitalMapMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        signageMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateServiceRequestItems(true, QuickNavigationMenuButtons.QuickNavigationMenu.SIGNAGE));
        signageMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        databaseMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateServiceRequestItems(true, QuickNavigationMenuButtons.QuickNavigationMenu.DATABASE));
        databaseMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());
    }
}
