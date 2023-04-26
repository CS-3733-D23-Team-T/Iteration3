package edu.wpi.tacticaltritons.auth;

import edu.wpi.tacticaltritons.database.Login;
import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;

public class Authenticator {

    public static Object requestAuthentication(AuthenticationMethod method, Login requester){
        switch (method){
            case EMAIL -> {
                return ConfirmEmail.sendConfirmationEmail(requester.getEmail());
            }
            case APP -> {
                //TODO do something
//                String secret = requester.getAppSecret();
                String secret = "";
                ConfirmApp.generateTOTPThread(secret);
            }
            case PHONE -> {
                //TODO implement me
            }
        }
        return false;
    }
    public static String generateRandomHexString(){
        byte[] bytes = new byte[64];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return Hex.encodeHexString(bytes);
    }
}
