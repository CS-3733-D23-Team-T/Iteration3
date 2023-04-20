package edu.wpi.tacticaltritons.pathfinding;


/*public class AStarAlgorithm {
    private final Congestion congestion;

    public AStarAlgorithm(Congestion congestion) {
        this.congestion = congestion;
    }

    // ... other methods ...

    public List<Node> findShortestPath(Node startNode, Node endNode) throws SQLException {
        // ... existing implementation ...

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(endNode)) {
                return reconstructPath(cameFrom, startNode, current);
            }

            for (Node neighbor : neighborMap.getOrDefault(current, Collections.emptySet())) {
                if (neighbor.equals(current) || visitedNodes.contains(neighbor)) {
                    continue;
                }

                double congestionFactor = 1 + congestion.getCongestion(current, neighbor) / 100.0;
                double tentativeGScore =
                        gScore.getOrDefault(current, Double.MAX_VALUE) + euclideanDistance(current, neighbor) * congestionFactor;
                double tentativeFScore = tentativeGScore + manhattanDistance(neighbor, endNode);

                // ... rest of the implementation ...
            }
        }

        return null;
    }

    // ... other methods ...
}*/
