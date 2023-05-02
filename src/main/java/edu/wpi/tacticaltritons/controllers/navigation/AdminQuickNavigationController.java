package edu.wpi.tacticaltritons.controllers.navigation;

import com.sun.java.accessibility.util.Translator;
import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;

import java.io.IOException;

public class AdminQuickNavigationController {
    @FXML private MenuButton serviceRequestMenuButton;
    @FXML private MenuButton hospitalMapMenuButton;
    @FXML private MenuButton signageMenuButton;
    @FXML private MenuButton databaseMenuButton;
    @FXML private MenuButton announcementsButton;

    @FXML
    private void initialize() throws IOException {
        serviceRequestMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(true, QuickNavigationMenuButtons.QuickNavigationMenu.SERVICE_REQUEST));
        serviceRequestMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        hospitalMapMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(true, QuickNavigationMenuButtons.QuickNavigationMenu.HOSPITAL_MAP));
        hospitalMapMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        signageMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(true, QuickNavigationMenuButtons.QuickNavigationMenu.SIGNAGE));
        signageMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        databaseMenuButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(true, QuickNavigationMenuButtons.QuickNavigationMenu.DATABASE));
        databaseMenuButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        announcementsButton.getItems().addAll(App.quickNavigationMenuButtons
                .generateMenuButton(true, QuickNavigationMenuButtons.QuickNavigationMenu.ANNOUNCEMENTS));
        announcementsButton.addEventHandler(EventType.ROOT,
                QuickNavigationMenuButtons.generateQuickNavEventHandler());

        serviceRequestMenuButton.setText(GoogleTranslate.getString("serviceRequest"));
        hospitalMapMenuButton.setText(GoogleTranslate.getString("hospitalMap"));
        signageMenuButton.setText(GoogleTranslate.getString("signage"));
        databaseMenuButton.setText(GoogleTranslate.getString("database"));
        announcementsButton.setText(GoogleTranslate.getString("announcements"));
    }
}
