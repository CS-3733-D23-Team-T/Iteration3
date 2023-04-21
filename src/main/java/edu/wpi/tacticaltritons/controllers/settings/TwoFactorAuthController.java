package edu.wpi.tacticaltritons.controllers.settings;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.AuthenticationMethod;
import edu.wpi.tacticaltritons.auth.Authenticator;
import edu.wpi.tacticaltritons.auth.ConfirmApp;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.SettingsNavigation;
import edu.wpi.tacticaltritons.navigation.TwoFactorNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TwoFactorAuthController {
    @FXML private ComboBox<String> twoFactorCombobox;
    @FXML private MFXButton twoFactorButton;
    @FXML private BorderPane rootPane;
    @FXML private MFXButton cancelButton;

    @FXML
    private void initialize() throws SQLException, IOException {
        Map<String, Screen> twoFactorOptions = new HashMap<>();
        twoFactorOptions.put(AuthenticationMethod.EMAIL.formalName(), Screen.TWO_FACTOR_EMAIL);
        twoFactorOptions.put(AuthenticationMethod.APP.formalName(), Screen.TWO_FACTOR_APP);
        twoFactorOptions.put(AuthenticationMethod.PHONE.formalName(), Screen.TWO_FACTOR_PHONE);

        Login login = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());
        if(login.getTwoFactor()){
            twoFactorButton.setText("Disable");
            if(login.getTwoFactorMethods() != null){
                System.out.println(login.getTwoFactorMethods()[0]);
                twoFactorCombobox.setValue(AuthenticationMethod.parseAuthenticationMethod(
                        login.getTwoFactorMethods()[0]).formalName());
            }
            else{
                twoFactorCombobox.setValue(AuthenticationMethod.EMAIL.formalName());
            }
            Screen contentPage = null;
            switch(AuthenticationMethod.parseAuthenticationMethod(
                    login.getTwoFactorMethods()[0]
            )){
                case EMAIL -> contentPage = Screen.TWO_FACTOR_EMAIL;
                case APP -> contentPage = Screen.TWO_FACTOR_APP;
                case PHONE -> contentPage = Screen.TWO_FACTOR_PHONE;
            }
            FXMLLoader loader = new FXMLLoader(App.class.getResource(contentPage.getFilename()));
            rootPane.setCenter(loader.load());
        }
        else{
            twoFactorCombobox.setDisable(true);
        }

//        twoFactorCombobox.setItems(FXCollections.observableList(twoFactorOptions.keySet().stream().toList()));
        twoFactorCombobox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if(!Objects.equals(o, n) && n != null || !Objects.equals(n, "") ||
                !TwoFactorNavigation.methodPage.get().formalName().equals(n)){

                System.out.println(n);
                TwoFactorNavigation.navigate(twoFactorOptions.get(AuthenticationMethod.parseAuthenticationMethod(n).formalName()));
            }
        });


        twoFactorButton.setOnAction(event -> {
            if(login.getTwoFactor()){
                twoFactorButton.setText("Enable");
                login.setTwoFactor(false);
                twoFactorCombobox.setValue("");
                login.setTwoFactorMethods(null);
                login.setLastLogin(null);
                rootPane.setCenter(null);
            }
            else{
                twoFactorButton.setText("Disable");
                twoFactorCombobox.setDisable(false);
                login.setTwoFactor(true);
                login.setLastLogin(LocalDateTime.now());
                twoFactorCombobox.setValue(AuthenticationMethod.EMAIL.name());
                login.setTwoFactorMethods(AuthenticationMethod.compileMethods(null,
                        AuthenticationMethod.EMAIL.name()));

                FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.TWO_FACTOR_EMAIL.getFilename()));
                try {
                    rootPane.setCenter(loader.load());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            UserSessionToken.userTFA.set(login.getTwoFactor());
            new Thread(() -> {
                try{
                    DAOFacade.updateLogin(login);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        cancelButton.setOnAction(event -> {
//            if(ConfirmApp.TOTPThread.isAlive()){
//                ConfirmApp.TOTPThread.interrupt();
//            }
            SettingsNavigation.navigate(Screen.USER_OPTIONS);
        });
    }
}
