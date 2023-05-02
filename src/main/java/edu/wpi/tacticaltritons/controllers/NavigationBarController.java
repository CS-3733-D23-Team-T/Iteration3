package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class NavigationBarController {
    @FXML private MFXButton backButton;
    @FXML private MFXButton homeButton;
    @FXML private MFXButton forwardButton;
    @FXML private Text pageTitle;
    @FXML private Text dateAndTime;
    @FXML private MenuButton menuButton;

    @FXML private Text accountTypeDisplay;
    @FXML private Text nameDisplay;

    private SimpleStringProperty dateTime;

    @FXML
    private void initialize() throws IOException {
        nameDisplay.textProperty().bind(UserSessionToken.fullNameProperty);
        if(UserSessionToken.getUser()!=null) {
            if (UserSessionToken.getUser().isAdmin()) {
                accountTypeDisplay.setText(GoogleTranslate.getString("admin"));
            } else {
                accountTypeDisplay.setText(GoogleTranslate.getString("staff"));
            }
        }

        Navigation.screen.addListener((obs, o ,n) -> {
            if(n != null) {
                boolean login = UserSessionToken.getUser() == null;
                if (login) {
                    accountTypeDisplay.setVisible(false);
                    nameDisplay.setVisible(false);
                    toggleNavigation(false);
                }
                else {
                    accountTypeDisplay.setVisible(true);
                    nameDisplay.setVisible(true);
                    toggleNavigation(true);
                }
            }
        });


        dateTime = new SimpleStringProperty();
        Thread dateTimeThread = fetchDateTime(1);
        dateTimeThread.setDaemon(true);
        dateTimeThread.start();

        this.forwardButton.setOnAction(event -> Navigation.goForward());
        this.backButton.setOnAction(event -> Navigation.goBackward());

        this.homeButton.setOnAction(event -> Navigation.navigate(Screen.HOME));

        this.pageTitle.textProperty().bind(Navigation.pageName);

        this.dateAndTime.textProperty().bind(this.dateTime);

        MenuItem settingsItem = new MenuItem(GoogleTranslate.getString("settings"));
        settingsItem.setOnAction(event -> Navigation.navigate(Screen.SETTINGS));
        MenuItem logoutItem = new MenuItem(GoogleTranslate.getString("logout"));
        logoutItem.setOnAction(event -> {
            UserSessionToken.revoke();
            Navigation.navigate(Screen.LOGIN);
        });
        MenuItem exitItem = new MenuItem(GoogleTranslate.getString("exit"));
        exitItem.setOnAction(event -> App.getPrimaryStage().close());
        MenuItem aboutItem = new MenuItem(GoogleTranslate.getString("about"));
        aboutItem.setOnAction(event -> Navigation.navigate(Screen.ABOUT));
        MenuItem creditsItem = new MenuItem(GoogleTranslate.getString("credits"));
        creditsItem.setOnAction(event -> Navigation.navigate(Screen.CREDITS));

        this.menuButton.getItems().add(settingsItem);
        this.menuButton.getItems().add(aboutItem);
        this.menuButton.getItems().add(creditsItem);
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

        toggleNavigation(false);
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

    private void toggleNavigation(boolean toggle){
        this.backButton.setDisable(!toggle);
        this.backButton.setVisible(toggle);

        this.homeButton.setDisable(!toggle);
        this.homeButton.setVisible(toggle);

        this.forwardButton.setDisable(!toggle);
        this.forwardButton.setVisible(toggle);

        this.menuButton.setDisable(!toggle);
        this.menuButton.setVisible(toggle);
    }
}
