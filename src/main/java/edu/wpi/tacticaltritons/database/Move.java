package edu.wpi.tacticaltritons.database;

import java.util.Date;

public class Move {
  private Node node;
  private LocationName location;
  private Date date;

  public Move(Node node, LocationName location, Date moveDate) {
    this.node = node;
    this.location = location;
    this.date = moveDate;
  }

  public Node getNode() {
    return node;
  }

  public void setNode(Node node) {
    this.node = node;
  }

  public LocationName getLocation() {
    return location;
  }

  public void setLocation(LocationName location) {
    this.location = location;
  }

  public Date getMoveDate() {
    return date;
  }

  public void setMoveDate(Date moveDate) {
    this.date = moveDate;
  }
}
