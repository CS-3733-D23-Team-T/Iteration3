package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.List;

public class TestDirections {

  public void testPosition() throws SQLException {
    AStarAlgorithm algorithm = new AStarAlgorithm();

    // Use node IDs 105 and 135 for testing
    int startNodeId = 840;
    int endNodeId = 640;

    Node startNode = DAOFacade.getNode(startNodeId);
    Node endNode = DAOFacade.getNode(endNodeId);

    List<Node> shortestPath = algorithm.findShortestPath(startNode, endNode);

    Directions directions = new Directions(shortestPath);

    List<String> dir = directions.position();

    if (shortestPath == null) {
    } else {
      for (Node node : shortestPath) {
      }
    }
  }
}
