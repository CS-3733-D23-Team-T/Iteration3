package edu.wpi.tacticaltritons.database;

public class Edge {
  private Node startNode;
  private Node endNode;

  private int congestion;

  public Edge(Node startNode, Node endNode) {
    this.startNode = startNode;
    this.endNode = endNode;
    this.congestion = 0;
  }

  public Edge(Node startNode, Node endNode, int congestion) {
    this.startNode = startNode;
    this.endNode = endNode;
    this.congestion = congestion;
  }

  public Node getStartNode() {
    return startNode;
  }

  public void setStartNode(Node startNode) {
    this.startNode = startNode;
  }

  public Node getEndNode() {
    return endNode;
  }

  public void setEndNode(Node endNode) {
    this.endNode = endNode;
  }

  public int getCongestion() {
    return congestion;
  }

  public void setCongestion(int congestion) {
    this.congestion = congestion;
  }
}
