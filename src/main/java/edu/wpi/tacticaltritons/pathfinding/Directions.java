package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;

import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class Directions {

    public Node currentNode;
    public Node nextNode;

    public Node futureNode;
    // public List<Node> nodeList;
    CongestionController congestion = new CongestionController();
    AStarAlgorithm algorithm = new AStarAlgorithm(congestion);
    // Use node IDs 105 and 135 for testing
    int startNodeId = 840;
    int endNodeId = 640;
    Node startNode = DAOFacade.getNode(startNodeId);
    Node endNode = DAOFacade.getNode(endNodeId);
    List<Move> allMoves = DAOFacade.getAllMoves();
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
                if (compass.facingNorth()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(1);
                }

                if (compass.facingEast()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(2);
                }
                if (compass.facingSouth()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(1);
                }

                if (compass.facingWest()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(0);
                }

                //when the x direction is bigger in the current x and y
            } else if (currentX > nextX && currentY > nextY) {
                if (compass.facingNorth()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(3);

                }
                if (compass.facingEast()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(0);
                }
                if (compass.facingSouth()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(3);
                }
                if (compass.facingWest()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(0);
                }

                //if we are in the same floor and just need to go straight
            } else if (currentX == nextX && currentY > nextY) {
                textDirections.add("Go Straight");
                // currentX < nextX && currentY < nextY
            } else if (currentX < nextX && currentY < nextY) {

                if (compass.facingNorth()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(1);
                }
                if (compass.facingEast()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(0);
                }
                if (compass.facingSouth()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(1);

                }
                if (compass.facingWest()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(0);
                }

            } else if (currentX > nextX && currentY < nextY) {

                if (compass.facingNorth()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(3);
                }
                if (compass.facingEast()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(0);
                }
                if (compass.facingSouth()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(3);
                }
                if (compass.facingWest()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(0);
                }

            } else if (currentX == nextX && currentY < nextY) {
                textDirections.add("Go Straight");

            } else if (currentX == nextX && currentY == nextY) {
                textDirections.add("You are already Here");
            } else if (currentX > nextX && currentY == nextY) {
                //textDirections.add("Go Straight");
                if (compass.facingNorth()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(3);
                }
                if (compass.facingEast()) {
                    textDirections.add("Turn Left");
                    compass.setCompass(0);
                }
                if (compass.facingSouth()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(1);
                }
                if (compass.facingWest()) {
                    textDirections.add("Turn Right");
                    compass.setCompass(0);
                }

            } else if (currentX < nextX && currentY == nextY) {
                textDirections.add("Go Straight");
            }

        }

        return textDirections;
    }

    public List<String> printDirections() {
        if (shortestPath == null || shortestPath.size() < 2) {

        }

        List<String> textDirections = new ArrayList<>();

        String nextLocation = "";
        for(Move move : allMoves){
            if(move.getNode().getNodeID() == shortestPath.get(1).getNodeID()){
                nextLocation = move.getLocation().getShortName();
            }
        }
        textDirections.add("Go Straight to " + nextLocation);


        for (int i = 1; i < shortestPath.size() - 1; i++) {
            currentNode = shortestPath.get(i - 1);
            nextNode = shortestPath.get(i);
            futureNode = shortestPath.get(i + 1);

            for(Move move : allMoves){
                if(move.getNode().getNodeID() == futureNode.getNodeID()){
                    nextLocation = move.getLocation().getShortName();
                }
            }


            int currentX = currentNode.getXcoord();
            int nextX = nextNode.getXcoord();
            int futureX = futureNode.getXcoord();

            int currentY = currentNode.getYcoord();
            int nextY = nextNode.getYcoord();
            int futureY = futureNode.getYcoord();
            double startAngle = Math.toDegrees(Math.atan2(nextY - currentY, nextX - currentX));
            double nextAngle = Math.toDegrees(Math.atan2(futureY - nextY, futureX - nextX));
            double angle = nextAngle - startAngle;


            if (angle > -20 && angle < 20) {
                if(textDirections.get(textDirections.size()-1).contains("Go Straight")){
                    textDirections.set(textDirections.size()-1, "Go Straight to " + nextLocation);
                }
                else{
                    textDirections.add("Go Straight to " + nextLocation);
                }
            } else if (angle > 20 && angle < 180) {
                textDirections.add("Turn Right");
                textDirections.add("Go Straight to " + nextLocation);
            } else if (angle < -20 && angle > -180) {
                textDirections.add("Turn Left");
                textDirections.add("Go Straight to " + nextLocation);
            }


            if (!nextNode.getFloor().equals(currentNode.getFloor())) {
                textDirections.add(futureNode.getFloor());
            }

        }

        textDirections.add("You have arrived at your location");

        return textDirections;
    }

//  public double adjustAngle(double angle){
//    while( angle <= 180 ){
//        angle += 360;
//    }
//    while(angle > 180){
//        angle -=360;
//    }
//
//    return angle;
//  }
//  public List<String> printPositions(){
//    if (shortestPath == null || shortestPath.size() < 2) {
//    }
//    List<String> textDirections = new ArrayList<>();
//    for (int i = 0; i < shortestPath.size() - 1; i++) {
//      currentNode = shortestPath.get(i);
//      nextNode = shortestPath.get(i + 1);
//
//      int currentX = currentNode.getXcoord();
//      int nextX = nextNode.getXcoord();
//      int currentY = currentNode.getYcoord();
//      int nextY = nextNode.getYcoord();
//      int xDiff = currentX - nextX;
//      int yDiff = currentY - nextY;
//
//
//      String position = "";
//
//      if (yDiff < 0){
//        position += "South";
//      }else if(yDiff > 0) {
//        position += "North";
//      } else if(yDiff == 0){
//        position += "";
//      }
//
//      if(xDiff < 0 ){
//        position += "West";
//      } else if(xDiff > 0){
//        position += "East";
//      }else if(xDiff == 0){
//        position += "";
//      }
//
//
//
//      System.out.println("Go" + position);
//      textDirections.add(position);
//    }
//      return textDirections;
//  }
}