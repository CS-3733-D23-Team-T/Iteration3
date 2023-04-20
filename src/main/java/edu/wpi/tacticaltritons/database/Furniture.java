package edu.wpi.tacticaltritons.database;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class Furniture {
  private int orderNum;
  private String firstName;
  private String lastName;
  private String assignedStaffFirst;
  private String assignedStaffLast;
  private Date deliveryDate;
  private String location;
  private String items;
  private RequestStatus status;

  public Furniture(String firstName,
                        String lastName,
                        String assignedStaffFirst,
                        String assignedStaffLast,
                        Date deliveryDate,
                        String location,
                        String items,
                        RequestStatus status) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.assignedStaffFirst = assignedStaffFirst;
    this.assignedStaffLast = assignedStaffLast;
    this.deliveryDate = deliveryDate;
    this.location = location;
    this.items = items;
    this.status = status;
  }
  public Furniture(
          int orderNum,
          String firstName,
          String lastName,
          String assignedStaffFirst,
          String assignedStaffLast,
          Date deliveryDate,
          String location,
          String items,
          RequestStatus status) {
    this.orderNum = orderNum;
    this.firstName = firstName;
    this.lastName = lastName;
    this.assignedStaffFirst = assignedStaffFirst;
    this.assignedStaffLast = assignedStaffLast;
    this.deliveryDate = deliveryDate;
    this.location = location;
    this.items = items;
    this.status = status;
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

  public String getAssignedStaffFirst() {
    return assignedStaffFirst;
  }

  public void setAssignedStaffFirst(String assignedStaffFirst) {
    this.assignedStaffFirst = assignedStaffFirst;
  }

  public String getAssignedStaffLast() {
    return assignedStaffLast;
  }

  public void setAssignedStaffLast(String assignedStaffLast) {
    this.assignedStaffLast = assignedStaffLast;
  }

  public Date getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(Date deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getItems() {
    return items;
  }

  public void setItems(String items) {
    this.items = items;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }

  public static void initTable() throws SQLException {
    String flowerTable =
        "CREATE TABLE FLOWER("
            + "orderNum SERIAL,"
            + "firstName varchar(50),"
            + "lastName varchar(50),"
            + "assignedStaffFirst varchar(50),"
            + "assignedStaffLast varchar(50),"
            + "deliveryDate Date,"
            + "location varchar(50),"
            + "items varchar(100),"
            + "status varchar(50),"
            + "CONSTRAINT FurnitureFormsPK PRIMARY KEY (orderNum),"
            + "CONSTRAINT destinationFK FOREIGN KEY (location) REFERENCES locationName(shortName)"
            + ");";
    //        Statement statement = Tdb.createStatement();
    //        statement.executeUpdate(flowerTable);
  }
}
