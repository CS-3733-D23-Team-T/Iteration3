package edu.wpi.tacticaltritons.database;

import java.sql.Date;

public class Invitations {
    private int conferenceID;
    private String firstName;
    private String lastName;
    private boolean accepted;
    private Date date;
    private String location;

    public Invitations(int conferenceID, String firstName, String lastName, boolean accepted, Date date, String location) {
        this.conferenceID = conferenceID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accepted = accepted;
        this.date = date;
        this.location = location;
    }

    public int getConferenceID() {
        return conferenceID;
    }

    public void setConferenceID(int conferenceID) {
        this.conferenceID = conferenceID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
