package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;

import java.sql.SQLException;
import java.util.*;

public class AStarAlgorithmHandicap implements PathFindingAlgorithm {

    private CongestionController congestionController;

    public AStarAlgorithmHandicap(CongestionController congestionController) {
        this.congestionController = congestionController;
    }

    public void setCongestionController(CongestionController congestionController) {
        this.congestionController = congestionController;
    }

    @Override
    public List<Node> findShortestPath(Node startNode, Node endNode) throws SQLException {
        Map<Node, Node> cameFrom = new HashMap<>();
        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();
        Set<Node> visitedNodes = new HashSet<>();
        List<Move> allMoves = precomputeAllMoves();

        HashMap<Integer, Move> moveHash = new HashMap<>();
        for (Move move : allMoves) {
            moveHash.put(move.getNode().getNodeID(), move);
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));

        openSet.add(startNode);
        gScore.put(startNode, 0.0);
        fScore.put(startNode, euclideanDistance(startNode, endNode, moveHash));

        List<Edge> edges = DAOFacade.getAllEdges();
        Map<Node, Set<Node>> neighborMap = precomputeNeighbors(edges);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(endNode)) {
                return reconstructPath(cameFrom, startNode, current);
            }

            for (Node neighbor : neighborMap.getOrDefault(current, Collections.emptySet())) {
                if (neighbor.equals(current) || visitedNodes.contains(neighbor) || isStairs(moveHash.get(neighbor.getNodeID()))) {
                    continue;
                }

                double tentativeGScore =
                        gScore.getOrDefault(current, Double.MAX_VALUE) + euclideanDistance(current, neighbor, moveHash);
                double tentativeFScore = tentativeGScore + manhattanDistance(neighbor, endNode);
                double congestionFactor = congestionController.getCongestionFactor(current, neighbor);
                tentativeFScore *= congestionFactor;

                if (openSet.contains(neighbor) && tentativeFScore >= fScore.get(neighbor)) {
                    continue;
                }

                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                fScore.put(neighbor, tentativeFScore);

                openSet.remove(neighbor);
                openSet.add(neighbor);
                visitedNodes.add(neighbor);
            }
        }

        return null;
    }

    private boolean isStairs(Move move) {
        return move.getLocation().getNodeType().equals("STAI");
    }

    private double euclideanDistance(Node a, Node b, HashMap<Integer, Move> hash) {
        int deltaX = a.getXcoord() - b.getXcoord();
        int deltaY = a.getYcoord() - b.getYcoord();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return distance;
    }


}