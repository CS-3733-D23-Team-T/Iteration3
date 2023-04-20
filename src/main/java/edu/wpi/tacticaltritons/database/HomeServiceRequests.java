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

    public HomeServiceRequests(String firstName, String lastName, String requestType, int orderNum, Date deliveryDate, Time deliveryTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.requestType = requestType;
        this.orderNum = orderNum;
        this.deliveryDate = deliveryDate;
        this.deliveryTime = deliveryTime;
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
}
