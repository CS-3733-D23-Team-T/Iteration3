package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.List;

public class TestBreadthFirstSearchAlgorithm {

    public static void main(String[] args) {
        TestBreadthFirstSearchAlgorithm test = new TestBreadthFirstSearchAlgorithm();
        try {
            test.testShortestPath();
        } catch (SQLException e) {
            System.err.println("Error while running the test:");
            e.printStackTrace();
        }
    }

    public void testShortestPath() throws SQLException {
        BreadthFirstSearchAlgorithm algorithm = new BreadthFirstSearchAlgorithm();

        // Use node IDs 105 and 980 for testing
        int startNodeId = 105;
        int endNodeId = 980;

        Node startNode = DAOFacade.getNode(startNodeId);
        Node endNode = DAOFacade.getNode(endNodeId);

        List<Node> shortestPath = algorithm.findShortestPath(startNode, endNode);

        if (shortestPath == null) {
            System.out.println("No path found.");
        } else {
            System.out.println("Shortest path:");
            for (Node node : shortestPath) {
                System.out.println(node.getNodeID());
            }
        }
    }
}
