package edu.wpi.tacticaltritons.navigation;

import edu.wpi.tacticaltritons.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SettingsNavigation {
    public static void navigate(final Screen destination){
        try{
            final var settings = App.class.getResource(Screen.SETTINGS.getFilename());
            final var resource = App.class.getResource(destination.getFilename());
            FXMLLoader loader = new FXMLLoader(settings);

            BorderPane rootPane = loader.load();
            loader = new FXMLLoader(resource);
            rootPane.setLeft(loader.load());

            App.getRootPane().setCenter(rootPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
