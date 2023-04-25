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
        MessageDigest digest;
        String hexPasswordHash;
        String hexSalt;
        try {
            digest = MessageDigest.getInstance("SHA-256");

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

            DAOFacade.addLogin(
                    new Login(username,
                            hexSalt,
                            hexPasswordHash,
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
        } catch (NoSuchAlgorithmException | SQLException e) {
            e.printStackTrace();
        }

        return -1;
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
                    System.out.println(lastLogin);
                    LocalDateTime currentTime = LocalDateTime.now();
                    System.out.println(currentTime);

                    Duration duration = Duration.between(lastLogin, currentTime);

                    long hours = duration.toHours();

                    String frequency = login.getTwoFactorFrequency();
                    if(frequency == null){
                        login.setTwoFactorFrequency(TwoFactorFrequency.DAILY.name());
                        UserSessionToken.registerToken(login);
                        DAOFacade.updateLogin(login);
                        return 2;
                    }
                    System.out.println(frequency + ", " + verifyDif(frequency, hours) + ", " + hours);
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
        MessageDigest digest;

        try{
            Login user = DAOFacade.getLogin(UserSessionToken.getUser().getUsername());

            if(!passwordMatch(UserSessionToken.getUser().getUsername(), plainTextOldPassword)){
                return 2;
            }

            //new password creation + salt
            byte[] newSalt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(newSalt);

            digest = MessageDigest.getInstance("SHA-256");
            digest.update(newSalt);

            StringBuilder builder = new StringBuilder();
            for(byte b : newSalt){
                builder.append(String.format("%02x", b));
            }
            user.setSalt(builder.toString());

            builder = new StringBuilder();
            byte[] newEncodedHash = digest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));
            digest.reset();
            digest.update(newSalt);
            for(byte b : newEncodedHash){
                builder.append(String.format("%02x", b));
            }
            user.setPassword(builder.toString());


            DAOFacade.deleteLogin(DAOFacade.getLogin(UserSessionToken.getUser().getUsername()));
            DAOFacade.addLogin(user);
            return 1;
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void resetPassword(String plainTextPassword, String email) {
        Login user;
        try {
            user = DAOFacade.getAllLogins().parallelStream().filter(x -> x.getEmail().equals(email)).collect(
                   Collectors.collectingAndThen(Collectors.toList(), list -> list.get(0)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(user == null){
            throw new RuntimeException("Couldn't find user");
        }

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            digest.update(salt);
            byte[] encodedHash = digest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            for(byte b : salt){
                builder.append(String.format("%02x", b));
            }
            user.setSalt(builder.toString());
            builder = new StringBuilder();
            for(byte b : encodedHash){
                builder.append(String.format("%02x", b));
            }
            user.setPassword(builder.toString());

            new Thread(() -> {
                try {
                    DAOFacade.updateLogin(user);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
