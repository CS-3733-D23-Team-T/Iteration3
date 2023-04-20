package edu.wpi.tacticaltritons.database;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;

public class Flower {
  private int orderNum;
  private String requesterFirst;
  private String requesterLast;
  private String patientFirst;
  private String patientLast;
  private String assignedStaffFirst;
  private String assignedStaffLast;
  private Date deliveryDate;
  private Time deliveryTime;
  private String location;
  private String items;
  private int total;
  private RequestStatus status;

  public Flower(String requesterFirst,
          String requesterLast,
          String patientFirst,
          String patientLast,
          String assignedStaffFirst,
          String assignedStaffLast,
          Date deliveryDate,
          Time deliveryTime,
          String location,
          String items,
          int total,
          RequestStatus status) {
    this.requesterFirst = requesterFirst;
    this.requesterLast = requesterLast;
    this.patientFirst = patientFirst;
    this.patientLast = patientLast;
    this.assignedStaffFirst = assignedStaffFirst;
    this.assignedStaffLast = assignedStaffLast;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.location = location;
    this.items = items;
    this.total = total;
    this.status = status;
  }
  public Flower(
          int orderNum,
          String requesterFirst,
          String requesterLast,
          String patientFirst,
          String patientLast,
          String assignedStaffFirst,
          String assignedStaffLast,
          Date deliveryDate,
          Time deliveryTime,
          String location,
          String items,
          int total,
          RequestStatus status) {
    this.orderNum = orderNum;
    this.requesterFirst = requesterFirst;
    this.requesterLast = requesterLast;
    this.patientFirst = patientFirst;
    this.patientLast = patientLast;
    this.assignedStaffFirst = assignedStaffFirst;
    this.assignedStaffLast = assignedStaffLast;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.location = location;
    this.items = items;
    this.total = total;
    this.status = status;
  }
  public int getOrderNum() {
    return orderNum;
  }

  public String getRequesterFirst() {
    return requesterFirst;
  }

  public void setRequesterFirst(String requesterFirst) {
    this.requesterFirst = requesterFirst;
  }

  public String getRequesterLast() {
    return requesterLast;
  }

  public void setRequesterLast(String requesterLast) {
    this.requesterLast = requesterLast;
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

  public Time getDeliveryTime() {
    return deliveryTime;
  }

  public void setDeliveryTime(Time deliveryTime) {
    this.deliveryTime = deliveryTime;
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

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
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
            + "requesterFirst varchar(225),"
            + "requesterLast varchar(225),"
            + "patientFirst varchar(225),"
            + "patientLast varchar(225),"
            + "assignedStaffFirst varchar(225),"
            + "assignedStaffLast varchar(225),"
            + "deliveryDate Date,"
            + "deliveryTime Time,"
            + "location varchar(225),"
            + "items varchar(225),"
            + "total numeric(5),"
            + "status varchar(225),"
            + "CONSTRAINT flowerPK PRIMARY KEY (orderNum),"
            + "CONSTRAINT destinationFK FOREIGN KEY (location) REFERENCES locationName(shortName)"
            + ");";
    //        Statement statement = Tdb.createStatement();
    //        statement.executeUpdate(flowerTable);
  }
}
