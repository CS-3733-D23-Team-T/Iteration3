package edu.wpi.tacticaltritons.database;

import java.sql.Date;
import java.sql.SQLException;

public class Conference {
    private int orderNum;
    private String firstName;
    private String lastName;
    private Date date;
    private String attendance;
    private int expectedSize;
    private String location;
    private RequestStatus status;

    public Conference(int orderNum, String firstName, String lastName, java.sql.Date date, String attendance, int expectedSize, String location, RequestStatus status) {
        this.orderNum = orderNum;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.attendance = attendance;
        this.expectedSize = expectedSize;
        this.location = location;
        this.status = status;
    }

    public Conference(String firstName, String lastName, java.sql.Date date, String attendance, int expectedSize, String location, RequestStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.attendance = attendance;
        this.expectedSize = expectedSize;
        this.location = location;
        this.status = status;
    }
    public static void initTable() throws SQLException {
        String conferenceTable =
                "CREATE TABLE CONFERENCE("
                        + "orderNum SERIAL, "
                        + "firstName varchar(25), "
                        + "lastName varchar(25), "
                        + "date Date, "
                        + "attendance varchar(225), "
                        + "expectedSize numeric(5)"
                        + "location varchar(50),"
                        + "status varchar(50), "
                        + "CONSTRAINT conferencePK PRIMARY KEY(orderNum),"
                        + "CONSTRAINT locationFK FOREIGN KEY (location) REFERENCES locationname(longname));";
        //        Statement statement = Tdb.createStatement();
        //        statement.executeUpdate(flowerTable);
    }

    public int getExpectedSize() {
        return expectedSize;
    }

    public void setExpectedSize(int expectedSize) {
        this.expectedSize = expectedSize;
    }

    public int getOrderNum() {
        return orderNum;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
