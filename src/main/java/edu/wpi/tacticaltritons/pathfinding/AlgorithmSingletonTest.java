package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.List;

public class AlgorithmSingletonTest {

    public static void main(String[] args) {
        AlgorithmSingleton singleton = AlgorithmSingleton.getInstance();

        int startNodeId = 105;
        int endNodeId = 1080;

        try {
            Node startNode = DAOFacade.getNode(startNodeId);
            Node endNode = DAOFacade.getNode(endNodeId);

            // Test AStarAlgorithm
//            singleton.setAlgorithm(new AlgorithmSingleton.AStarSingleton());
//            printPath("AStar Algorithm Path", singleton.findShortestPath(startNode, endNode));
//
//            // Test DepthFirstSearchAlgorithm
//            singleton.setAlgorithm(new AlgorithmSingleton.DepthFirstSearchSingleton());
//            printPath("Depth-First Search Algorithm Path", singleton.findShortestPath(startNode, endNode));
//
//            // Test BreadthFirstSearchAlgorithm
//            singleton.setAlgorithm(new AlgorithmSingleton.BreadthFirstSearchSingleton());
//            printPath("Breadth-First Search Algorithm Path", singleton.findShortestPath(startNode, endNode));

        } catch (SQLException e) {
            System.err.println("Error occurred while finding path: " + e.getMessage());
        }
    }

    private static void printPath(String title, List<Node> shortestPath) {
        System.out.println(title);
        if (shortestPath == null) {
            System.out.println("No path found.");
        } else {
            System.out.println("Shortest path:");
            for (Node node : shortestPath) {
                System.out.println(node.getNodeID());
            }
        }
        System.out.println();
    }
}
