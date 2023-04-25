package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

public enum AlgorithmSingleton {
    ASTAR(new AStarAlgorithm(new CongestionController())),
    DEPTH_FIRST_SEARCH(new DepthFirstSearchAlgorithm()),
    BREADTH_FIRST_SEARCH(new BreadthFirstSearchAlgorithm()),
    DIJKSTRA(new DijkstraAlgorithm());

    private static AlgorithmSingleton INSTANCE = ASTAR;
    public final PathFindingAlgorithm algorithm;

    AlgorithmSingleton(PathFindingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public static AlgorithmSingleton getInstance() {
        return INSTANCE;
    }

    public static void setInstance(AlgorithmSingleton algorithm) {
        INSTANCE = algorithm;
    }

    public static AlgorithmSingleton parseAlgorithm(String algorithm) {
        if (algorithm == null) return null;
        algorithm = algorithm.toUpperCase(Locale.ROOT);
        algorithm = algorithm.replaceAll(" ", "_");
        if (algorithm.equals(ASTAR.name())) return ASTAR;
        else if (algorithm.equals(DEPTH_FIRST_SEARCH.name())) return DEPTH_FIRST_SEARCH;
        else if (algorithm.equals(BREADTH_FIRST_SEARCH.name())) return BREADTH_FIRST_SEARCH;
        else if (algorithm.equals(DIJKSTRA.name())) return DIJKSTRA;
        return null;
    }

    public String formalName() {
        String[] parts = this.name().split("_");
        StringBuilder formalName = new StringBuilder();
        for (String part : parts) {
            formalName.append(part.charAt(0))
                    .append(part.toLowerCase(Locale.ROOT).substring(1))
                    .append(" ");
        }
        return formalName.substring(0, formalName.length() - 1);
    }
}
