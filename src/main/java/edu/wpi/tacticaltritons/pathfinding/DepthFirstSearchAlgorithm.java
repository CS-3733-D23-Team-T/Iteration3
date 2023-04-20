package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.*;

public class DepthFirstSearchAlgorithm implements PathFindingAlgorithm{

    @Override
    public List<Node> findShortestPath(Node startNode, Node endNode) throws SQLException {
        Map<Node, Node> cameFrom = new HashMap<>();
        Set<Node> visitedNodes = new HashSet<>();
        Deque<Node> stack = new LinkedList<>();

        stack.push(startNode);
        visitedNodes.add(startNode);

        List<Edge> edges = DAOFacade.getAllEdges();
        Map<Node, Set<Node>> neighborMap = precomputeNeighbors(edges);

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (current.equals(endNode)) {
                return reconstructPath(cameFrom, startNode, current);
            }

            for (Node neighbor : neighborMap.getOrDefault(current, Collections.emptySet())) {
                if (neighbor.equals(current) || visitedNodes.contains(neighbor)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                visitedNodes.add(neighbor);
                stack.push(neighbor);
            }
        }

        return null;
    }
}
