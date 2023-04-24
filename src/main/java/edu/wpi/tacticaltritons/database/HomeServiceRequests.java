package edu.wpi.tacticaltritons.database;

import java.sql.Date;
import java.sql.Time;

public class HomeServiceRequests {
        private String firstName;
        private String lastName;
        private String requestType;
        private int orderNum;
        private Date deliveryDate;
        private Time deliveryTime;
        private String patientFirst;
        private String patientLast;
        private String items;
        private String location;

    public HomeServiceRequests(String firstName, String lastName, String requestType, int orderNum, Date deliveryDate, Time deliveryTime, String patientFirst, String patientLast, String items, String location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.requestType = requestType;
        this.orderNum = orderNum;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
        this.patientFirst = patientFirst;
        this.patientLast = patientLast;
        this.items = items;
        this.location = location;
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

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Time getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Time delieveryTime) {
        this.deliveryTime = delieveryTime;
    }

    public String getPatientFirst() {
        return patientFirst;
    }

    public void setPatientFirst(String patientFirst) {
        this.patientFirst = patientFirst;
    }

    public String getPatientLast() {
        return patientLast;
    }

    public void setPatientLast(String patientLast) {
        this.patientLast = patientLast;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
