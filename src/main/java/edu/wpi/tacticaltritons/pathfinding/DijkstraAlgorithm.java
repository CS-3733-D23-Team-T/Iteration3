package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.*;

public class DijkstraAlgorithm implements PathFindingAlgorithm {

    @Override
    public List<Node> findShortestPath(Node startNode, Node endNode) throws SQLException {
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Double> distance = new HashMap<>();
        Set<Node> visitedNodes = new HashSet<>();

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(distance::get));

        openSet.add(startNode);
        distance.put(startNode, 0.0);

        List<Edge> edges = DAOFacade.getAllEdges();
        Map<Node, Set<Node>> neighborMap = precomputeNeighbors(edges);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(endNode)) {
                return reconstructPath(cameFrom, startNode, current);
            }

            for (Node neighbor : neighborMap.getOrDefault(current, Collections.emptySet())) {
                if (neighbor.equals(current) || visitedNodes.contains(neighbor)) {
                    continue;
                }

                double tentativeDistance = distance.getOrDefault(current, Double.MAX_VALUE) + euclideanDistance(current, neighbor);

                if (tentativeDistance < distance.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    distance.put(neighbor, tentativeDistance);

                    openSet.remove(neighbor);
                    openSet.add(neighbor);
                }
            }

            visitedNodes.add(current);
        }

        return null;
    }


    private double euclideanDistance(Node a, Node b) {
        int deltaX = a.getXcoord() - b.getXcoord();
        int deltaY = a.getYcoord() - b.getYcoord();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

}
