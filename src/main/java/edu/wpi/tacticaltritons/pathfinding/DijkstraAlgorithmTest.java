package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.List;

public class DijkstraAlgorithmTest {

    public static void main(String[] args) {
        int startNodeId = 105;
        int endNodeId = 980;

        // Fetch the start and end nodes using the DAOFacade
        Node startNode = null;
        Node endNode = null;
        try {
            startNode = DAOFacade.getNode(startNodeId);
            endNode = DAOFacade.getNode(endNodeId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (startNode == null || endNode == null) {
            System.out.println("Failed to fetch start or end nodes.");
            return;
        }

        // Instantiate the DijkstraAlgorithm
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm();

        // Call findShortestPath and store the result in a List
        List<Node> shortestPath = null;
        try {
            shortestPath = dijkstraAlgorithm.findShortestPath(startNode, endNode);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // If the shortest path is found, print out the node IDs
        if (shortestPath != null) {
            System.out.println("Shortest path:");
            for (Node node : shortestPath) {
                System.out.println("Node ID: " + node.getNodeID());
            }
        } else {
            System.out.println("No path found.");
        }
    }
}
