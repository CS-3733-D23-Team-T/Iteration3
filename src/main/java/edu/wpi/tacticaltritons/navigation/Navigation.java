package edu.wpi.tacticaltritons.navigation;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.ArrayList;

public class Navigation {
    public static final Screen DEFAULT_SCREEN = Screen.LOGIN;

    public static ArrayList<Screen> history = new ArrayList<>();
    public static ArrayList<Screen> forwardHistory = new ArrayList<>();
    private static boolean checker = false;
    public static SimpleStringProperty pageName = new SimpleStringProperty(DEFAULT_SCREEN.formatScreenName());
    public static SimpleObjectProperty<Screen> screen = new SimpleObjectProperty<>(DEFAULT_SCREEN);

    public static void navigate(Screen destination) {
        if(UserSessionToken.getUser() == null
                && destination != Screen.SIGNAGE
                && destination != Screen.VIEW_MAP
                && destination != Screen.PATHFINDING
                && destination != Screen.THREE_D_MAP){
            destination = DEFAULT_SCREEN;
        }
        if(destination.equals(screen.get())) return;
        if(destination.equals(DEFAULT_SCREEN)){
            history.clear();
            forwardHistory.clear();
        }
        else {
            if (checker) {
                forwardHistory.clear();
            }
            checker = true;
            if (history.size() != 0) {
                if (!(history.get(history.size() - 1).equals(destination))) {
                    history.add(destination);
                }
            }
            else {
                history.add(destination);
            }
        }
        try {
            pageName.set(destination.formatScreenName());
            screen.set(destination);
            final var resource = App.class.getResource(destination.getFilename());
            final FXMLLoader loader = new FXMLLoader(resource);
            App.getRootPane().getChildren().clear();

            try {
                App.getRootPane().setCenter(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ExceptionInInitializerError ignored) { }

            if(UserSessionToken.getUser() == null){
                App.getNavBar().setCenter(App.getLoginQuickNavigation());
            }
            else{
                if(UserSessionToken.getUser().isAdmin()){
                    App.getNavBar().setCenter(App.getAdminQuickNavigation());
                }
                else{
                    App.getNavBar().setCenter(App.getStaffQuickNavigation());
                }
            }
            App.getRootPane().setTop(App.getNavBar());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static void goForward() {
        if(forwardHistory.size() >= 1){
            history.add(forwardHistory.get(forwardHistory.size()-1));
            checker = false;
            navigate(forwardHistory.get(forwardHistory.size()-1));
            forwardHistory.remove(forwardHistory.size()-1);
        }
    }

    public static void goBackward() {
        if(history.size() > 1){
            forwardHistory.add(history.get(history.size()-1));
            history.remove(history.size()-1);
            checker = false;
            navigate(history.get(history.size()-1));
        }
    }
}
