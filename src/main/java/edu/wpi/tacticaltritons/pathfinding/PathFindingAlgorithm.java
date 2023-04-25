package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;

import java.sql.SQLException;
import java.util.*;

public interface PathFindingAlgorithm {
    List<Node> findShortestPath(Node startNode, Node endNode) throws SQLException;

    default Map<Node, Set<Node>> precomputeNeighbors(List<Edge> edges) {
        Map<Node, Set<Node>> neighborMap = new HashMap<>();
        for (Edge edge : edges) {
            neighborMap.computeIfAbsent(edge.getStartNode(), k -> new HashSet<>()).add(edge.getEndNode());
            neighborMap.computeIfAbsent(edge.getEndNode(), k -> new HashSet<>()).add(edge.getStartNode());
        }
        return neighborMap;
    }

    default List<Node> reconstructPath(Map<Node, Node> cameFrom, Node startNode, Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null && !current.equals(startNode)) {
            path.add(current);
            current = cameFrom.get(current);
        }
        path.add(startNode); // Add the start node to the path
        Collections.reverse(path);
        return path;
    }

    default List<Move> precomputeAllMoves() throws SQLException {
        return DAOFacade.getAllMoves();
    }


}
