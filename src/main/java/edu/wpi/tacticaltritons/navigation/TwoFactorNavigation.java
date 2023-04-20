package edu.wpi.tacticaltritons.navigation;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.AuthenticationMethod;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class TwoFactorNavigation {
    public static SimpleObjectProperty<AuthenticationMethod> methodPage = new SimpleObjectProperty<>(AuthenticationMethod.EMAIL);
    public static void navigate(final Screen destination){
        try{
            final var settings = App.class.getResource(Screen.SETTINGS.getFilename());
            final var twoFactor = App.class.getResource(Screen.TWO_FACTOR_AUTH.getFilename());
            final var dest = App.class.getResource(destination.getFilename());
            switch (destination){
                case TWO_FACTOR_EMAIL -> methodPage.set(AuthenticationMethod.EMAIL);
                case TWO_FACTOR_APP -> methodPage.set(AuthenticationMethod.APP);
                case TWO_FACTOR_PHONE -> methodPage.set(AuthenticationMethod.PHONE);
            }
            FXMLLoader loader = new FXMLLoader(settings);

            BorderPane settingsPane = loader.load();
            loader = new FXMLLoader(twoFactor);
            BorderPane twoFactorPane = loader.load();
            loader = new FXMLLoader(dest);

            twoFactorPane.setCenter(loader.load());
            settingsPane.setLeft(twoFactorPane);
            App.getRootPane().setCenter(settingsPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
