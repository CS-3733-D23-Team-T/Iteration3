package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;

import java.io.IOException;
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
    public QuickNavigationMenuButtons() throws IOException {
        serviceRequestNavigationMap.clear();
        serviceRequestNavigationMap.put(GoogleTranslate.getString("mealRequest"), Screen.MEAL_RESTAURANT);
        serviceRequestNavigationMap.put(GoogleTranslate.getString("flowerRequest"), Screen.FLOWER_CHOICE);
        serviceRequestNavigationMap.put(GoogleTranslate.getString("furnitureRequest"), Screen.FURNITURE_DELIVERY);
        serviceRequestNavigationMap.put(GoogleTranslate.getString("officeSupplyRequest"), Screen.SUPPLY_REQUEST);
        serviceRequestNavigationMap.put(GoogleTranslate.getString("conferenceRoomRequest"), Screen.CONFERENCE_ROOM);
        serviceRequestNavigationMap.put(GoogleTranslate.getString("viewServiceRequest"), Screen.VIEW_SERVICE_REQUEST);

        hospitalMapNavigationMap.clear();
        hospitalMapNavigationMap.put(GoogleTranslate.getString("hospitalMap"), Screen.VIEW_MAP);
        hospitalMapNavigationMap.put(GoogleTranslate.getString("editHospitalMap"), Screen.EDIT_MAP);
        hospitalMapNavigationMap.put(GoogleTranslate.getString("pathfinding"), Screen.PATHFINDING);
        hospitalMapNavigationMap.put(GoogleTranslate.getString("moveLocation"), Screen.MOVE);

        signageNavigationMap.clear();
        signageNavigationMap.put(GoogleTranslate.getString("signage"), Screen.SIGNAGE);
        signageNavigationMap.put(GoogleTranslate.getString("editSignage"), Screen.EDIT_SIGNAGE);
        signageNavigationMap.put(GoogleTranslate.getString("moveSignage"), Screen.MOVE_SIGN);

        databaseNavigationMap.clear();
        databaseNavigationMap.put(GoogleTranslate.getString("database"), Screen.DATABASE);
        databaseNavigationMap.put(GoogleTranslate.getString("databaseHelp"), Screen.DATABASE_HELP);

        announcementNavigationMap.clear();
        announcementNavigationMap.put(GoogleTranslate.getString("createAnnouncements"), Screen.CREATE_ANNOUNCEMENTS);
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

                if(admin || (!key.equals("Edit Hospital Map") && !key.equals("Move Location"))) {
                    MenuItem item = new MenuItem(key);
                    item.setOnAction(event -> Navigation.navigate(value));
                    list.add(item);
                }
            });
            case SIGNAGE -> signageNavigationMap.forEach((key, value) -> {
                if(admin || (!key.equals("Edit Signage") &&  !key.equals("Move Signage"))){
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
