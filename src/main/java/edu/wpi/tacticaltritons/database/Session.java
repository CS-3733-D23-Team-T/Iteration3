package edu.wpi.tacticaltritons.database;

import java.util.UUID;

public class Session {
    private String username;
    private String location;
    private String firstname;
    private String lastname;
    private String email;
    private boolean admin;
    private int sessionTime;
    private final UUID tokenId;
    private boolean userTFA;

    public Session(
            String username,
            String location,
            String firstname,
            String lastname,
            String email,
            boolean admin,
            int sessionTime,
            UUID tokenId,
            boolean userTFA){
        this.username = username;
        this.location = location;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.admin = admin;
        this.sessionTime = sessionTime;
        this.tokenId = tokenId;
        this.userTFA = userTFA;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setSessionTime(int sessionTime) {
        this.sessionTime = sessionTime;
    }

    public int getSessionTime() {
        return sessionTime;
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public boolean getUserTFA(){
        return this.userTFA;
    }

    public void setUserTFA(boolean userTFA) {
        this.userTFA = userTFA;
    }
}
