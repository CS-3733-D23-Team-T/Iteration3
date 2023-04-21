package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NavigationBarController {
    @FXML private MFXButton backButton;
    @FXML private MFXButton homeButton;
    @FXML private MFXButton forwardButton;
    @FXML private Text pageTitle;
    @FXML private Text dateAndTime;
    @FXML private MenuButton menuButton;

    @FXML private MenuButton serviceRequestMenuButton;
    @FXML private MenuButton hospitalMapMenuButton;
    @FXML private MenuButton signageMenuButton;
    @FXML private MenuButton databaseMenuButton;

    @FXML private Text accountTypeDisplay;
    @FXML private Text nameDisplay;

    @FXML private FlowPane quickNavigationPane;


    private SimpleStringProperty dateTime;

    private boolean navigationToggle = false;
    Map<String, Screen> serviceRequestNavigationMap = new HashMap<>();
    Map<String, Screen> hospitalMapNavigationMap = new HashMap<>();
    Map<String, Screen> signageNavigationMap = new HashMap<>();
    Map<String, Screen> databaseNavigationMap = new HashMap<>();


    @FXML
    private void initialize() {
        nameDisplay.textProperty().bind(UserSessionToken.fullNameProperty);

        quickNavigationPane.widthProperty().addListener((obs, o, n) -> {
            if(o != null && o.doubleValue() != 0 && n != null) {
                quickNavigationPane.setHgap((quickNavigationPane.getHgap() / o.doubleValue()) * n.doubleValue());
            }
            else{
                quickNavigationPane.setHgap((quickNavigationPane.getHgap() / 1280) * 1280);
            }
        });

        Navigation.screen.addListener((obs, o ,n) -> {
            if(n != null) {
                boolean login = n.equals(Screen.LOGIN) || n.equals(Screen.SIGNAGE) || n.equals(Screen.VIEW_MAP);
                if (login) {
                    navigationToggle = false;
                    accountTypeDisplay.setVisible(false);
                    nameDisplay.setVisible(false);
                    toggleLoginNavigation();
                }
                else {
                    navigationToggle = true;
                    accountTypeDisplay.setVisible(true);
                    nameDisplay.setVisible(true);
                    if(UserSessionToken.getUser() != null && UserSessionToken.getUser().isAdmin()){
                        accountTypeDisplay.setText("Admin");
                        toggleAdminNavigation();
                    }
                    else{
                        accountTypeDisplay.setText("Staff");
                        toggleStaffNavigation();
                    }
                }
            }
        });

        serviceRequestNavigationMap.put("Meal Request", Screen.MEAL_RESTAURANT);
        serviceRequestNavigationMap.put("Flower Request", Screen.FLOWER_CHOICE);
        serviceRequestNavigationMap.put("Furniture Request", Screen.FURNITURE_DELIVERY);
        serviceRequestNavigationMap.put("Office Supply Request", Screen.OFFICE_SUPPLIES);
        serviceRequestNavigationMap.put("Conference Room Request", Screen.CONFERENCE_ROOM);
        serviceRequestNavigationMap.put("View Service Request", Screen.VIEW_SERVICE_REQUEST);


        hospitalMapNavigationMap.put("Hospital Map", Screen.VIEW_MAP);
        hospitalMapNavigationMap.put("Edit Hospital Map", Screen.EDIT_MAP);
        hospitalMapNavigationMap.put("Pathfinding", Screen.PATHFINDING);

        signageNavigationMap.put("Signage", Screen.SIGNAGE);
        signageNavigationMap.put("Edit Signage", null);

        databaseNavigationMap.put("Database", Screen.DATABASE);
        databaseNavigationMap.put("Edit Database", Screen.EDIT_DATABASE);
        databaseNavigationMap.put("Database Help", Screen.DATABASE_HELP);

        dateTime = new SimpleStringProperty();
        Thread dateTimeThread = fetchDateTime(1);
        dateTimeThread.setDaemon(true);
        dateTimeThread.start();

        this.forwardButton.setOnAction(event -> Navigation.goForward());
        this.backButton.setOnAction(event -> Navigation.goBackward());

        this.homeButton.setOnAction(event -> Navigation.navigate(Screen.HOME));

        this.pageTitle.textProperty().bind(Navigation.pageName);

        this.dateAndTime.textProperty().bind(this.dateTime);

        MenuItem settingsItem = new MenuItem("Settings");
        settingsItem.setOnAction(event -> Navigation.navigate(Screen.SETTINGS));
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(event -> {
            UserSessionToken.revoke();
            Navigation.navigate(Screen.LOGIN);
        });
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> App.getPrimaryStage().close());

        this.menuButton.getItems().add(settingsItem);
        this.menuButton.getItems().add(logoutItem);
        this.menuButton.getItems().add(exitItem);
        RotateTransition rotate = new RotateTransition();
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setCycleCount(1);
        rotate.setDuration(Duration.millis(200));
        rotate.setNode(this.menuButton);
        this.menuButton.setOnShowing(event -> {
            rotate.setByAngle(90);
            rotate.play();
        });
        this.menuButton.setOnHidden(event -> {
            rotate.setByAngle(-90);
            rotate.play();
        });

        mapMenuButton(serviceRequestNavigationMap, serviceRequestMenuButton);
        serviceRequestMenuButton.addEventHandler(EventType.ROOT,
                generateQuickNavEventHandler());

        mapMenuButton(hospitalMapNavigationMap, hospitalMapMenuButton);
        hospitalMapMenuButton.addEventHandler(EventType.ROOT,
                generateQuickNavEventHandler());

        mapMenuButton(signageNavigationMap, signageMenuButton);
        signageMenuButton.addEventHandler(EventType.ROOT,
                generateQuickNavEventHandler());

        mapMenuButton(databaseNavigationMap, databaseMenuButton);
        databaseMenuButton.addEventHandler(EventType.ROOT,
                generateQuickNavEventHandler());

        toggleNavigation(false);
    }

    private EventHandler<? super Event> generateQuickNavEventHandler(){
        return event -> {
            if(event.getEventType().equals(MenuButton.ON_SHOWN)){
                ColorAdjust shadow = new ColorAdjust();
                shadow.setBrightness(-.6);
                App.getRootPane().getCenter().setEffect(shadow);
                App.getRootPane().getCenter().setDisable(true);
            }
            else if(event.getEventType().equals(MenuButton.ON_HIDDEN)){
                App.getRootPane().getCenter().setEffect(null);
                App.getRootPane().getCenter().setDisable(false);
            }
        };
    }
    /**
     * Precision: 1 for min, 2 for second
     *
     * @return a thread that fetches the date every precision
     */
    private Thread fetchDateTime(int precision) {

// Make one thread and have it update itself over and over again
        return new Thread(
                () -> {
                    AtomicBoolean running = new AtomicBoolean(true);
                    while (running.get()) {
                        DateTimeFormatter dateTimeFormatter;
                        if (precision == 1) dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");
                        else dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss");
                        LocalDateTime localDateTime = LocalDateTime.now();

                        List<String> dateTimeComponents =
                                new ArrayList<>(List.of(dateTimeFormatter.format(localDateTime).split(" ")));
                        dateTimeComponents.add(
                                0,
                                localDateTime.getDayOfWeek().toString().charAt(0)
                                        + localDateTime
                                        .getDayOfWeek()
                                        .toString()
                                        .substring(1, 3)
                                        .toLowerCase(Locale.ROOT));

                        dateTimeComponents.set(
                                2,
                                dateTimeComponents
                                        .get(2)
                                        .replaceAll("0+.*", dateTimeComponents.get(2).substring(1)));

                        dateTimeComponents.set(3, formatStandardTime(dateTimeComponents.get(3)));

                        StringBuilder dateTime = new StringBuilder();
                        dateTimeComponents.forEach(value -> dateTime.append(value).append(" "));

                        this.dateTime.set(dateTime.substring(0, dateTime.length() - 1));

                        try {
                            App.getPrimaryStage()
                                    .setOnCloseRequest(
                                            event -> {
                                                running.set(false);
                                            });
                            if (!running.get()) break;
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private static String formatStandardTime(String militaryTime) {
        int hours = Integer.parseInt(militaryTime.substring(0, militaryTime.indexOf(":")));
        String ending = hours < 12 ? "AM" : "PM";
        String hoursString =
                hours - 12 == 0
                        ? "12"
                        : hours - 12 < 0 ? String.valueOf(hours) : String.valueOf(hours - 12);

        return hoursString + militaryTime.substring(militaryTime.indexOf(":")) + " " + ending;
    }

    private void mapMenuButton(Map<String, Screen> map, MenuButton button){
        map.keySet().forEach(key -> {
            MenuItem item = new MenuItem(key);
            item.setOnAction(event -> Navigation.navigate(map.get(key)));
            button.getItems().add(item);
        });
    }
    private void mapMenuItem(String name, Screen destination, MenuButton button){
        MenuItem item = new MenuItem(name);
        item.setOnAction(event -> Navigation.navigate(destination));
        button.getItems().add(item);
    }

    private void toggleNavigation(boolean toggle){
        this.backButton.setDisable(!toggle);
        this.backButton.setVisible(toggle);

        this.homeButton.setDisable(!toggle);
        this.homeButton.setVisible(toggle);

        this.forwardButton.setDisable(!toggle);
        this.forwardButton.setVisible(toggle);

        this.menuButton.setDisable(!toggle);
        this.menuButton.setVisible(toggle);

        this.serviceRequestMenuButton.setDisable(!toggle);
        this.serviceRequestMenuButton.setVisible(toggle);
    }
    private void toggleLoginNavigation(){
        toggleNavigation(false);

        this.databaseMenuButton.setDisable(true);
        this.databaseMenuButton.setVisible(false);

        this.hospitalMapMenuButton.setDisable(false);
        this.hospitalMapMenuButton.setVisible(true);
        hospitalMapMenuButton.getItems().removeIf(item -> item.getText().equals("Edit Hospital Map"));

        this.signageMenuButton.setDisable(false);
        this.signageMenuButton.setVisible(true);
        signageMenuButton.getItems().removeIf(item -> item.getText().equals("Edit Signage"));
    }
    private void toggleStaffNavigation(){
        toggleNavigation(true);

        this.databaseMenuButton.setDisable(true);
        this.databaseMenuButton.setVisible(false);

        this.hospitalMapMenuButton.setDisable(false);
        this.hospitalMapMenuButton.setVisible(true);
        hospitalMapMenuButton.getItems().removeIf(item -> item.getText().equals("Edit Hospital Map"));

        this.signageMenuButton.setDisable(false);
        this.signageMenuButton.setVisible(true);
        signageMenuButton.getItems().removeIf(item -> item.getText().equals("Edit Signage"));
    }
    private void toggleAdminNavigation(){
        toggleNavigation(true);

        this.databaseMenuButton.setDisable(true);
        this.databaseMenuButton.setVisible(false);

        this.hospitalMapMenuButton.setDisable(false);
        this.hospitalMapMenuButton.setVisible(true);
        if(hospitalMapMenuButton.getItems().stream().noneMatch(item -> item.getText().equals("Edit Hospital Map"))){
            mapMenuItem("Edit Hospital Map", hospitalMapNavigationMap.get("Edit Hospital Map"), hospitalMapMenuButton);
        }

        this.signageMenuButton.setDisable(false);
        this.signageMenuButton.setVisible(true);
        if(signageMenuButton.getItems().stream().noneMatch(item -> item.getText().equals("Edit Signage"))){
            mapMenuItem("Edit Signage", hospitalMapNavigationMap.get("Edit Signage"), signageMenuButton);
        }
    }
}
