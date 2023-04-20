package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Directions {

  public Node currentNode;
  public Node nextNode;
  // public List<Node> nodeList;
  AStarAlgorithm algorithm = new AStarAlgorithm();
  // Use node IDs 105 and 135 for testing
  int startNodeId = 840;
  int endNodeId = 640;
  Node startNode = DAOFacade.getNode(startNodeId);
  Node endNode = DAOFacade.getNode(endNodeId);
  Compass compass = new Compass(0);
  List<Node> shortestPath = algorithm.findShortestPath(startNode, endNode);

  public Directions(List<Node> shortestPath) throws SQLException {
    this.shortestPath = shortestPath;
  }

  public List<String> position() {
    if (shortestPath == null || shortestPath.size() < 2) {
    }
    List<String> textDirections = new ArrayList<>();
    for (int i = 0; i < shortestPath.size() - 1; i++) {
      currentNode = shortestPath.get(i);
      nextNode = shortestPath.get(i + 1);

      int currentX = currentNode.getXcoord();
      int nextX = nextNode.getXcoord();
      int currentY = currentNode.getYcoord();
      int nextY = nextNode.getYcoord();


      // if current x is lees than next x and current y is greater than next y
      if (currentX < nextX && currentY > nextY) {
        if(compass.facingNorth()){
          textDirections.add("Turn Right");
          compass.setCompass(1);
        }

        if(compass.facingEast()){
          textDirections.add("Turn Right");
          compass.setCompass(2);
        }
        if(compass.facingSouth()){
          textDirections.add("Turn Left");
          compass.setCompass(1);
        }

        if(compass.facingWest()){
          textDirections.add("Turn Right");
          compass.setCompass(0);
        }

        //when the x direction is bigger in the current x and y
      } else if (currentX > nextX && currentY > nextY) {
        if(compass.facingNorth()){
          textDirections.add("Turn Left");
          compass.setCompass(3);

        }
        if(compass.facingEast()){
          textDirections.add("Turn Left");
          compass.setCompass(0);
        }
        if(compass.facingSouth()){
          textDirections.add("Turn Right");
          compass.setCompass(3);
        }
        if(compass.facingWest()){
          textDirections.add("Turn Right");
          compass.setCompass(0);
        }

        //if we are in the same floor and just need to go straight
      } else if (currentX == nextX && currentY > nextY) {
        textDirections.add("Go Straight");
        // currentX < nextX && currentY < nextY
      } else if (currentX < nextX && currentY < nextY) {

        if(compass.facingNorth()){
          textDirections.add("Turn Right");
          compass.setCompass(1);
        }
        if(compass.facingEast()){
          textDirections.add("Turn Left");
          compass.setCompass(0);
        }
        if(compass.facingSouth()){
          textDirections.add("Turn Left");
          compass.setCompass(1);

        }
        if(compass.facingWest()){
          textDirections.add("Turn Right");
          compass.setCompass(0);
        }

      } else if (currentX > nextX && currentY < nextY) {

        if(compass.facingNorth()){
          textDirections.add("Turn Left");
          compass.setCompass(3);
        }
        if(compass.facingEast()){
          textDirections.add("Turn Left");
          compass.setCompass(0);
        }
        if(compass.facingSouth()){
          textDirections.add("Turn Right");
          compass.setCompass(3);
        }
        if(compass.facingWest()){
          textDirections.add("Turn Right");
          compass.setCompass(0);
        }

      } else if (currentX == nextX && currentY < nextY) {
        textDirections.add("Go Straight");

      } else if (currentX == nextX && currentY == nextY) {
        textDirections.add("You are already Here");
      } else if(currentX > nextX && currentY == nextY){
       if(compass.facingNorth()){
         textDirections.add("Turn Left");
         compass.setCompass(3);
       }
        if(compass.facingEast()){
          textDirections.add("Turn Left");
          compass.setCompass(0);
        }
        if(compass.facingSouth()){
          textDirections.add("Turn Right");
          compass.setCompass(1);
        }
        if(compass.facingWest()){
          textDirections.add("Turn Right");
          compass.setCompass(0);
        }

      } else if(currentX < nextX && currentY == nextY){
        textDirections.add("Go Straight");
      }

    }

    return textDirections;
  }
}
