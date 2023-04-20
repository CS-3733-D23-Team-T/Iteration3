package edu.wpi.tacticaltritons.auth;

import edu.wpi.tacticaltritons.database.Login;
import org.apache.commons.codec.binary.Hex;

import java.security.SecureRandom;
import java.util.Locale;

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
    public static AuthenticationMethod parseAuthenticationMethod(String twoFactorMethod){
        if(twoFactorMethod == null) return null;
        twoFactorMethod = twoFactorMethod.toUpperCase(Locale.ROOT);
        if(AuthenticationMethod.EMAIL.name().equals(twoFactorMethod)) return AuthenticationMethod.EMAIL;
        if(AuthenticationMethod.APP.name().equals(twoFactorMethod)) return AuthenticationMethod.APP;
        if(AuthenticationMethod.PHONE.name().equals(twoFactorMethod)) return AuthenticationMethod.PHONE;
        return null;
    }
}
