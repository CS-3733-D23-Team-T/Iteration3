package edu.wpi.tacticaltritons;

import com.google.zxing.WriterException;
import edu.wpi.tacticaltritons.auth.Authenticator;
import edu.wpi.tacticaltritons.auth.ConfirmApp;
import edu.wpi.tacticaltritons.auth.ConfirmEmail;
import edu.wpi.tacticaltritons.database.DAOFacade;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

public class AuthTest {

    @Test
    public void confirmationEmailTest(){
        ConfirmEmail.sendConfirmationEmail("1jimmy.nuetron69@gmail.com");
    }

    @Test
    public void loginTimeTest(){
        LocalDate lastDate = LocalDate.of(2023,4,16);

        long days = ChronoUnit.DAYS.between(lastDate, LocalDate.now());

        System.out.println(days);
    }
    @Test
    public void QRCodePathTest() throws SQLException, WriterException {
//        ConfirmApp.findQRPath();
        String path = ConfirmApp.findQRPath();
        File f = Path.of(path).toFile();
        System.out.println(path);
        String secret = ConfirmApp.generateSecretKey();
        System.out.println(secret);
        String url = ConfirmApp.getGoogleAuthenticatorBarCode(secret, "1jimmy.nuetron69@gmail.com", "CS3733");
        ConfirmApp.createQRCode(url, path, 100,100);
//        Authenticator.requestAuthentication(Authenticator.AuthenticationMethod.APP, DAOFacade.getLogin("p4blo"));
    }
}
