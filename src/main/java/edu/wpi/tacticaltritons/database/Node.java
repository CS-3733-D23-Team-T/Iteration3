package edu.wpi.tacticaltritons.database;

import java.util.Objects;

public class Node {
  private int nodeID;

  private int xcoord;
  private int ycoord;
  private String floor;
  private String building;

  public Node(int nodeID, int xcoord, int ycoord, String floor, String building) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
  }

  public int getNodeID() {
    return nodeID;
  }

  public int getXcoord() {
    return xcoord;
  }

  public void setXcoord(int xcoord) {
    this.xcoord = xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public void setYcoord(int ycoord) {
    this.ycoord = ycoord;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public void setNodeID(Integer id){
    this.nodeID = id;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Node node = (Node) o;
    return nodeID == node.nodeID
        && xcoord == node.xcoord
        && ycoord == node.ycoord
        && Objects.equals(floor, node.floor)
        && Objects.equals(building, node.building);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeID, xcoord, ycoord, floor, building);
  }
}
