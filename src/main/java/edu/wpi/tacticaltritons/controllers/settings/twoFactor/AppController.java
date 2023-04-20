package edu.wpi.tacticaltritons.controllers.settings.twoFactor;

import com.google.zxing.WriterException;
import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.AuthenticationMethod;
import edu.wpi.tacticaltritons.auth.ConfirmApp;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.navigation.TwoFactorNavigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class AppController {
    @FXML private ImageView qrCodeImage;
    @FXML private Text codeText;
    @FXML private MFXTextField codeField;
    @FXML private MFXButton confirmButton;

    @FXML
    private void initialize() throws SQLException, WriterException {
        Login user = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());
        boolean unbound = !Arrays.asList(user.getTwoFactorMethods()).contains(AuthenticationMethod.APP.name());
        if(unbound){
            String path = ConfirmApp.findQRPath();
            String secretKey = ConfirmApp.generateSecretKey();
            ConfirmApp.generateTOTPThread(secretKey);
            String googleCode = ConfirmApp.getGoogleAuthenticatorBarCode(secretKey, UserSessionToken.getUser().getEmail(), "CS3733");
            qrCodeImage.setImage(ConfirmApp.createQRCode(googleCode, path, 175, 175));
            codeText.setVisible(false);
            codeField.setVisible(true);
            confirmButton.setVisible(true);
        }
        else{
            codeText.setVisible(true);
            confirmButton.setText("Unbind");
        }
        confirmButton.setOnAction(event -> {
            if(unbound) {
                if (ConfirmApp.attemptVerify(codeField.getText())) {
                    codeText.setVisible(true);
                    codeText.setText("BOUND");
                    qrCodeImage.setVisible(false);
                    codeField.setVisible(false);
                    confirmButton.setVisible(false);
                    user.setTwoFactorMethods(AuthenticationMethod.compileMethods(
                            user.getTwoFactorMethods(),
                            AuthenticationMethod.APP.name()
                    ));
                    TwoFactorNavigation.navigate(Screen.TWO_FACTOR_APP);
                }
                new Thread(() -> {
                    try {
                        DAOFacade.updateLogin(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
            else{
                user.setTwoFactorMethods(AuthenticationMethod.removeMethod(
                        user.getTwoFactorMethods(), AuthenticationMethod.APP.name()
                ));
                new Thread(() -> {
                    try {
                        DAOFacade.updateLogin(user);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                TwoFactorNavigation.navigate(Screen.TWO_FACTOR_APP);
            }
        });
    }
}
