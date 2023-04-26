package edu.wpi.tacticaltritons.auth;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class Account {
    private static String[] createHexPassword(String plaintextPassword){
        MessageDigest digest;
        String hexPasswordHash;
        String hexSalt;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();

            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            digest.update(salt);

            byte[] encodedHash = digest.digest(plaintextPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for (byte b : encodedHash) {
                builder.append(String.format("%02x", b));
            }

            hexPasswordHash = builder.toString();

            builder = new StringBuilder();
            for (byte b : salt) {
                builder.append(String.format("%02x", b));
            }
            hexSalt = builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        return new String[]{hexPasswordHash, hexSalt};
    }

    public static int createUser(
            String username,
            String plaintextPassword,
            String email,
            String firstname,
            String lastname,
            boolean admin)
            throws SQLException {
        int validationResult = Validator.validate(DAOFacade.getAllLogins(), email);
        if(validationResult != 1){
            return validationResult;
        }


        // start create new account

        String[] passwordTuple = createHexPassword(plaintextPassword);

        DAOFacade.addLogin(
                new Login(username,
                        passwordTuple[0],
                        passwordTuple[1],
                        email,
                        firstname,
                        lastname,
                        admin,
                        null,
                        false,
                        "en-us",
                        false,
                        null,
                        null,
                        UserSessionToken.DEFAULT_SESSION_TIME,
                        AlgorithmSingleton.ASTAR.name(),
                        false));

        return 1;

    }

    //ret values:
    // 1 success
    // -1 failure
    // 2 requires authentication
    public static int attemptLogin(String username, String plaintextPassword){
        if(passwordMatch(username, plaintextPassword)){
            try {
                Login login = DAOFacade.getLogin(username);

                if (login.getTwoFactor()) {
                    LocalDateTime lastLogin = login.getLastLogin();
                    LocalDateTime currentTime = LocalDateTime.now();

                    Duration duration = Duration.between(lastLogin, currentTime);

                    long hours = duration.toHours();

                    String frequency = login.getTwoFactorFrequency();
                    if(frequency == null){
                        login.setTwoFactorFrequency(TwoFactorFrequency.DAILY.name());
                        UserSessionToken.registerToken(login);
                        DAOFacade.updateLogin(login);
                        return 2;
                    }
                    if (verifyDif(frequency, hours)) {
                        return 2;
                    }
                }

                UserSessionToken.registerToken(login);
                DAOFacade.updateLogin(login);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return 1;
        }
        return -1;
    }

    private static boolean verifyDif(String frequency, long hours){
        switch(TwoFactorFrequency.parseFrequency(frequency)){
            case DAILY -> {
                return TwoFactorFrequency.DAILY.getHours() <= ((int) hours);
            }
            case HOURLY -> {
                return TwoFactorFrequency.HOURLY.getHours() <= ((int) hours);
            }
            default -> {
                return true;
            }
        }
    }

    private static boolean passwordMatch(String username, String plaintextPassword){
        MessageDigest digest;
        try {
            Login user = DAOFacade.getLogin(username);
            if(user == null) return false;
            digest = MessageDigest.getInstance("SHA-256");

            byte[] salt = new byte[user.getSalt().length() / 2];
            String hexSalt = user.getSalt();

            for (int i = 0; i < salt.length; i++) {
                int index = i * 2;

                int val = Integer.parseInt(hexSalt.substring(index, index + 2), 16);
                salt[i] = (byte) val;
            }

            digest.update(salt);
            byte[] encodedHash = digest.digest(plaintextPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for (byte b : encodedHash) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString().equals(user.getPassword());
        } catch (NoSuchAlgorithmException | SQLException e) {
            return false;
        }
    }

    //-1 failure
    //1 = success
    //2 = oldPasswords dont match
    public static int updatePassword(String plainTextPassword, String plainTextOldPassword){

        if(UserSessionToken.getUser() == null) return -1;
        if(!passwordMatch(UserSessionToken.getUser().getUsername(), plainTextOldPassword)){
            return 2;
        }

        new Thread(() -> {
            try {
                Login user = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());

                String[] passwordTuple = createHexPassword(plainTextPassword);
                user.setPassword(passwordTuple[0]);
                user.setSalt(passwordTuple[1]);

                DAOFacade.updateLogin(user);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();

        return 1;
    }

    // 1 is success
    // 2 is old password = new password
    // 3 no user with that email
    // -1 fail
    public static int resetPassword(String plainTextPassword, String email) {
        Login user = null;
        try {
            user = DAOFacade.getAllLogins().parallelStream().filter(x -> x.getEmail().equals(email)).collect(
                   Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0)));
        } catch (SQLException ignored) { }
        if(user == null){
            return 3;
        }
        if(passwordMatch(user.getUsername(), plainTextPassword)) return 2;


        String[] passwordTuple = createHexPassword(plainTextPassword);
        user.setPassword(passwordTuple[0]);
        user.setSalt(passwordTuple[1]);

        Login finalUser = user;
        new Thread(() -> {
            try {
                DAOFacade.updateLogin(finalUser);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();

        return 1;
    }
}
