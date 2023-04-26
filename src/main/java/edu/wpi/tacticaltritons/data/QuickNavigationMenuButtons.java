package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickNavigationMenuButtons {
    private final static Map<String, Screen> serviceRequestNavigationMap = new HashMap<>();
    private final static Map<String, Screen> hospitalMapNavigationMap = new HashMap<>();
    private final static Map<String, Screen> signageNavigationMap = new HashMap<>();
    private final static Map<String, Screen> databaseNavigationMap = new HashMap<>();
    private final static Map<String, Screen> announcementNavigationMap = new HashMap<>();

    public enum QuickNavigationMenu{SERVICE_REQUEST, HOSPITAL_MAP, SIGNAGE, DATABASE, ANNOUNCEMENTS}
    public QuickNavigationMenuButtons(){
        serviceRequestNavigationMap.put("Meal Request", Screen.MEAL_RESTAURANT);
        serviceRequestNavigationMap.put("Flower Request", Screen.FLOWER_CHOICE);
        serviceRequestNavigationMap.put("Furniture Request", Screen.FURNITURE_DELIVERY);
        serviceRequestNavigationMap.put("Office Supply Request", Screen.SUPPLY_CHOICE);
        serviceRequestNavigationMap.put("Conference Room Request", Screen.CONFERENCE_ROOM);
        serviceRequestNavigationMap.put("View Service Request", Screen.VIEW_SERVICE_REQUEST);


        hospitalMapNavigationMap.put("Hospital Map", Screen.VIEW_MAP);
        hospitalMapNavigationMap.put("Edit Hospital Map", Screen.EDIT_MAP);
        hospitalMapNavigationMap.put("Pathfinding", Screen.PATHFINDING);

        signageNavigationMap.put("Signage", Screen.SIGNAGE);
        signageNavigationMap.put("Edit Signage", Screen.EDIT_SIGNAGE);
        signageNavigationMap.put("Move Signage", Screen.MOVE_SIGN);

        databaseNavigationMap.put("Database", Screen.DATABASE);
        databaseNavigationMap.put("Edit Database", Screen.EDIT_DATABASE);
        databaseNavigationMap.put("Database Help", Screen.DATABASE_HELP);

        announcementNavigationMap.put("Edit Announcements", Screen.EDIT_ANNOUNCEMENTS);
        announcementNavigationMap.put("Create Announcements", Screen.CREATE_ANNOUNCEMENTS);
    }
    public List<MenuItem> generateMenuButton(boolean admin, QuickNavigationMenu menu){
        List<MenuItem> list = new ArrayList<>();
        switch(menu){
            case SERVICE_REQUEST -> serviceRequestNavigationMap.forEach((key, value) -> {
                MenuItem item = new MenuItem(key);
                item.setOnAction(event -> Navigation.navigate(value));
                list.add(item);
            });
            case HOSPITAL_MAP -> hospitalMapNavigationMap.forEach((key, value) -> {

                if(admin || !key.equals("Edit Hospital Map")) {
                    MenuItem item = new MenuItem(key);
                    item.setOnAction(event -> Navigation.navigate(value));
                    list.add(item);
                }
            });
            case SIGNAGE -> signageNavigationMap.forEach((key, value) -> {
                if(admin || !key.equals("Edit Signage")){
                    MenuItem item = new MenuItem(key);
                    item.setOnAction(event -> Navigation.navigate(value));
                    list.add(item);
                }
            });
            case DATABASE -> databaseNavigationMap.forEach((key, value) -> {
                if(admin){
                    MenuItem item = new MenuItem(key);
                    item.setOnAction(event -> Navigation.navigate(value));
                    list.add(item);
                }
            });
            case ANNOUNCEMENTS -> announcementNavigationMap.forEach((key, value) -> {
                if(admin){
                    MenuItem item = new MenuItem(key);
                    item.setOnAction(event -> Navigation.navigate(value));
                    list.add(item);
                }
            });
        }
        return list;
    }
    public static EventHandler<? super Event> generateQuickNavEventHandler(){
        return event -> {
            if(event.getEventType().equals(MenuButton.ON_SHOWN)){
                ColorAdjust shadow = new ColorAdjust();
                shadow.setBrightness(-.6);
                App.getRootPane().getCenter().setEffect(shadow);
                App.getRootPane().getCenter().setStyle("-fx-background-color: rgba(102,102,102,0.6)");
            }
            else if(event.getEventType().equals(MenuButton.ON_HIDDEN)){
                App.getRootPane().getCenter().setEffect(null);
                App.getRootPane().getCenter().setStyle(null);
                App.getRootPane().getCenter().setDisable(false);
            }
        };
    }
}
