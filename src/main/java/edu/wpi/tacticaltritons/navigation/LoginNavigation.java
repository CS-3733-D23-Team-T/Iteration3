package edu.wpi.tacticaltritons.navigation;

import edu.wpi.tacticaltritons.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LoginNavigation {
    private static String packet;

    public static void attachPacket(String username){
        if(packet == null) {
            packet = username;
        }
    }
    public static String retrievePacket(){
        String temp = packet;
        packet = null;
        return temp;
    }

    public static void navigate(final Screen destination){
        try{
            final var login = App.class.getResource(Screen.LOGIN.getFilename());
            final var resource = App.class.getResource(destination.getFilename());
            FXMLLoader loader = new FXMLLoader(login);

            BorderPane rootPane = loader.load();
            loader = new FXMLLoader(resource);
            rootPane.setRight(loader.load());

            App.getRootPane().setCenter(rootPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
