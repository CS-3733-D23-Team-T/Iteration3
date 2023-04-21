package edu.wpi.tacticaltritons.auth;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.database.Session;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import javafx.beans.property.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.SQLException;
import java.util.UUID;

public class UserSessionToken {
    private static Session user;
    public static BooleanProperty userTFA = new SimpleBooleanProperty(false);
    public static StringProperty fullNameProperty = new SimpleStringProperty("");
    public static BooleanProperty adminProperty = new SimpleBooleanProperty(false);
    //in ms
    private static int session_time;
    public static int DEFAULT_SESSION_TIME = 3600000; // one hour
    private static boolean active_token;

    public static void revoke(){
        adminProperty.set(false);
        fullNameProperty.set("");
        userTFA.set(false);
        user = null;
        active_token = false;
    }

    //TODO implement location data
    public static void registerToken(Login login) throws SQLException {
        if(login.getTokenTime() == 0) {
            login.setTokenTime(DEFAULT_SESSION_TIME);
            DAOFacade.updateLogin(login);
        }
        session_time = login.getTokenTime();
        AlgorithmSingleton.setInstance(AlgorithmSingleton.parseAlgorithm(login.getAlgorithmPreference()));
        user = new Session(
                login.getUsername(),
                null,
                login.getFirstName(),
                login.getLastName(),
                login.getEmail(),
                login.isAdmin(),
                session_time,
                UUID.randomUUID(),
                login.getTwoFactor());

        adminProperty.set(user.isAdmin());
        userTFA.set(login.getTwoFactor());
        fullNameProperty.set(user.getFirstname() + " " + user.getLastname());

        Session oldSession = DAOFacade.getSession(login.getUsername());
        if(oldSession != null){
            DAOFacade.deleteSession(oldSession);
        }
        DAOFacade.addSession(user);
        Thread session = new Thread(() -> {
            while(session_time != 0 || user != null || active_token){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                session_time -= 1000;
                if(user == null) break;
                user.setSessionTime(session_time);
                try {
                    DAOFacade.updateSession(user);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            revoke();
        });
        session.setDaemon(true);
        if(!active_token) {
            session.start();
            active_token = true;
        }
    }

    public static void setUser(Session newUser){
        user = newUser;
        fullNameProperty.set(user.getFirstname() + ", " + user.getLastname());
        adminProperty.set(user.isAdmin());
    }

    public static Session getUser(){
        return user;
    }

    public static void updateSessionTime(){
        session_time = 3600000;
    }
    public static void updateSessionTime(int time){
        session_time = time;
    }
}
