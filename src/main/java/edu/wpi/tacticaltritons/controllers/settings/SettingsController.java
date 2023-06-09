package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.*;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.database.Session;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class SettingsController {
    @FXML private CheckBox requireTwoFactorCheckBox;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private ComboBox<String> twoFactorFrequencyComboBox;
    @FXML private ComboBox<String> tokenTimeComboBox;
    @FXML private ComboBox<String> algorithmPreferenceComboBox;
    @FXML private ComboBox<String> databaseComboBox;
    @FXML private Label languageText;
    @FXML private Label frequencyText;
    @FXML private Label algoText;
    @FXML private Label tokenText;
    @FXML private Text warningText;
    @FXML private Text preferenceText;
    @FXML private Label databaseConnectionLabel;

    @FXML
    private void initialize() throws SQLException, IOException {
        preferenceText.setText(GoogleTranslate.getString("preferences"));
        languageText.setText(GoogleTranslate.getString("language"));
        frequencyText.setText(GoogleTranslate.getString("twoFactorFrequency"));
        algoText.setText(GoogleTranslate.getString("algorithmPreference"));
        tokenText.setText(GoogleTranslate.getString("tokenTimeLimit"));
        warningText.setText(GoogleTranslate.getString("warningSettings"));
        requireTwoFactorCheckBox.setText(GoogleTranslate.getString("requireTwoFactor"));
        databaseConnectionLabel.setText(GoogleTranslate.getString("databaseConnection"));


        Login user = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());

        //todo implement me
        requireTwoFactorCheckBox.setDisable(true);

        //todo implement me
        List<String> languageList = new ArrayList<>();
        languageList.add(Language.English.formalName());
        languageList.add(Language.Spanish.formalName());
        languageList.add(Language.Chinese.formalName());
        languageList.add(Language.Korean.formalName());
        languageList.add(Language.Hindi.formalName());
        languageList.add(Language.Greek.formalName());
        languageList.add(Language.German.formalName());
        languageComboBox.setItems(FXCollections.observableList(languageList));
        languageComboBox.setValue(Language.parseLanguage(user.getLanguage()).formalName());

        languageComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if(!Objects.equals(n, o)){
                user.setLanguage(Language.parseLanguage(n.split(" -")[0]).formalName());
                GoogleTranslate.setLanguage(Language.parseLanguage(n.split(" -")[0]).getLanguage());
                new Thread(() -> {
                    try{
                        DAOFacade.updateLogin(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                FXMLLoader navLoader = new FXMLLoader(App.class.getResource("views/NavigationBar.fxml"));
                FXMLLoader adminLoader = new FXMLLoader(App.class.getResource("views/navigation/AdminQuickNavigation.fxml"));
                FXMLLoader staffLoader = new FXMLLoader(App.class.getResource("views/navigation/StaffQuickNavigation.fxml"));
                try {
                    App.quickNavigationMenuButtons = new QuickNavigationMenuButtons();
                    App.setNavBar(navLoader.load());
                    App.setAdminQuickNavigation(adminLoader.load());
                    App.setStaffQuickNavigation(staffLoader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                App.getPrimaryStage().setScene(App.getRootPane().getScene());
                App.getPrimaryStage().show();
                Navigation.navigate(Screen.HOME);
                Navigation.navigate(Screen.SETTINGS);
            }
        });

        List<String> frequencyList = new ArrayList<>();
        frequencyList.add(TwoFactorFrequency.DAILY.formalName());
        frequencyList.add(TwoFactorFrequency.HOURLY.formalName());
        frequencyList.add(TwoFactorFrequency.EVERY.formalName());
        twoFactorFrequencyComboBox.setItems(FXCollections.observableList(frequencyList));

        if(!user.getTwoFactor()) twoFactorFrequencyComboBox.setDisable(true);
        else {
            if(user.getTwoFactorFrequency() != null) {
                twoFactorFrequencyComboBox.setValue(TwoFactorFrequency.parseFrequency(user.getTwoFactorFrequency()).formalName());
            }
            else{
                twoFactorFrequencyComboBox.setValue(TwoFactorFrequency.DAILY.formalName());
            }
        }

        twoFactorFrequencyComboBox.disableProperty().bind(UserSessionToken.userTFA.not());
        twoFactorFrequencyComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if(!Objects.equals(n, o)){
                System.out.println(TwoFactorFrequency.parseFrequency(n).name());
                user.setTwoFactorFrequency(TwoFactorFrequency.parseFrequency(n).name());
                new Thread(() -> {
                    try {
                        DAOFacade.updateLogin(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        List<String> tokenTimeList = new ArrayList<>();
        tokenTimeList.add(TokenTime.SIXTY_MINUTES.formalName());
        tokenTimeList.add(TokenTime.FORTY_FIVE_MINUTES.formalName());
        tokenTimeList.add(TokenTime.THIRTY_MINUTES.formalName());
        tokenTimeList.add(TokenTime.FIFTEEN_MINUTES.formalName());
        tokenTimeList.add(TokenTime.FIVE_MINUTES.formalName());
        tokenTimeList.add(TokenTime.FOREVER.formalName());
        tokenTimeComboBox.setItems(FXCollections.observableList(tokenTimeList));
        tokenTimeComboBox.setValue(TokenTime.parseTokenTime(user.getTokenTime()).formalName());

        tokenTimeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if(!Objects.equals(n, o)){
                user.setTokenTime(TokenTime.parseTokenTime(n).getTokenTime());
                UserSessionToken.updateSessionTime(TokenTime.parseTokenTime(n).getTokenTime());
                new Thread(() -> {
                    try{
                        DAOFacade.updateLogin(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        List<String> algorithmList = new ArrayList<>();
        algorithmList.add(AlgorithmSingleton.ASTAR.formalName());
        algorithmList.add(AlgorithmSingleton.BREADTH_FIRST_SEARCH.formalName());
        algorithmList.add(AlgorithmSingleton.DEPTH_FIRST_SEARCH.formalName());
        algorithmList.add(AlgorithmSingleton.DIJKSTRA.formalName());
        if(user.getAlgorithmPreference() == null){
            user.setAlgorithmPreference(AlgorithmSingleton.ASTAR.name());
            DAOFacade.updateLogin(user);
        }
        algorithmPreferenceComboBox.setItems(FXCollections.observableList(algorithmList));
        algorithmPreferenceComboBox.setValue(AlgorithmSingleton.parseAlgorithm(user.getAlgorithmPreference()).formalName());

        algorithmPreferenceComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if(!Objects.equals(n, o)){
                user.setAlgorithmPreference(AlgorithmSingleton.parseAlgorithm(n).name());
                AlgorithmSingleton.setInstance(AlgorithmSingleton.parseAlgorithm(n));
                new Thread(() -> {
                    try{
                        DAOFacade.updateLogin(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });

        List<String> databaseList = new ArrayList<>();
        databaseList.add(Tdb.WPI_DATABASE.formalName());
        databaseList.add(Tdb.AWS_DATABASE.formalName());

        databaseComboBox.setItems(FXCollections.observableList(databaseList));
        Tdb database = Tdb.parseTdb(user.getDatabase());
        if(database == null){
            database = Tdb.getInstance();
            new Thread(() -> {
                user.setDatabase(Tdb.getInstance().name());
                try {
                    DAOFacade.updateLogin(user);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        databaseComboBox.getSelectionModel().select(database.formalName());

        databaseComboBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if(!Objects.equals(n, o)){
                if(n.equals(Tdb.WPI_DATABASE.formalName())){
                    Tdb.setInstance(Tdb.WPI_DATABASE);
                }
                else if(n.equals(Tdb.AWS_DATABASE.formalName())){
                    Tdb.setInstance(Tdb.AWS_DATABASE);
                }
            }
            user.setDatabase(Tdb.getInstance().name());
            new Thread(() -> {
                try {
                    DAOFacade.updateLogin(user);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }
}
