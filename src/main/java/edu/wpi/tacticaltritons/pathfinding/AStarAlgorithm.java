package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.*;

public class AStarAlgorithm implements PathFindingAlgorithm{

  @Override
  public List<Node> findShortestPath(Node startNode, Node endNode) throws SQLException {
    Map<Node, Node> cameFrom = new HashMap<>();
    Map<Node, Double> gScore = new HashMap<>();
    Map<Node, Double> fScore = new HashMap<>();
    Set<Node> visitedNodes = new HashSet<>();
    List<Move> allMoves = precomputeAllMoves();

    HashMap <Integer, Move> moveHash = new HashMap<>();
    for (Move move: allMoves){
      moveHash.put(move.getNode().getNodeID(), move);
    }


    PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(fScore::get));

    openSet.add(startNode);
    gScore.put(startNode, 0.0);
    fScore.put(startNode, manhattanDistance(startNode, endNode));

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

        double tentativeGScore =
                gScore.getOrDefault(current, Double.MAX_VALUE) + manhattanDistance(current, neighbor);
        double tentativeFScore = tentativeGScore + manhattanDistance(neighbor, endNode);

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

  public Map<Node, Set<Node>> precomputeNeighbors(List<Edge> edges) {
    Map<Node, Set<Node>> neighborMap = new HashMap<>();
    for (Edge edge : edges) {
      neighborMap.computeIfAbsent(edge.getStartNode(), k -> new HashSet<>()).add(edge.getEndNode());
      neighborMap.computeIfAbsent(edge.getEndNode(), k -> new HashSet<>()).add(edge.getStartNode());
    }
    return neighborMap;
  }

  private double euclideanDistance(Node a, Node b, HashMap<Integer,Move> hash) {
    int deltaX = a.getXcoord() - b.getXcoord();
    int deltaY = a.getYcoord() - b.getYcoord();
    double result = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    if(hash.get(b.getNodeID()).getLocation().getNodeType().equals("STAI"))
    {
      result = 1.5 * result;
    }

    if(hash.get(b.getNodeID()).getLocation().getNodeType().equals("ELEV"))
    {
      result = .5 * result;
    }

    return result;
  }

  private double manhattanDistance(Node a, Node b) {
    int deltaX = Math.abs(a.getXcoord() - b.getXcoord());
    int deltaY = Math.abs(a.getYcoord() - b.getYcoord());
    return deltaX + deltaY;
  }


}

