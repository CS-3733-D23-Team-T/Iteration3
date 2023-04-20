package edu.wpi.tacticaltritons.controllers.settings.twoFactor;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import edu.wpi.tacticaltritons.auth.AuthenticationMethod;
import edu.wpi.tacticaltritons.auth.ConfirmEmail;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

public class EmailController {
    @FXML private Text emailText;

    @FXML
    private void initialize() throws SQLException, GeneralSecurityException, IOException {
        Login user = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());
        emailText.setText(UserSessionToken.getUser().getEmail());
        ConfirmEmail.getCredentials(GoogleNetHttpTransport.newTrustedTransport());
        user.setTwoFactorMethods(AuthenticationMethod.compileMethods(user.getTwoFactorMethods(), AuthenticationMethod.EMAIL.name()));
    }
}
