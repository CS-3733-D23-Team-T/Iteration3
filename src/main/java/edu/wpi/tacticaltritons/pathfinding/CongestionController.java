package edu.wpi.tacticaltritons.pathfinding;

import edu.wpi.tacticaltritons.database.Node;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class CongestionController {

    private Map<AbstractMap.SimpleImmutableEntry<Node, Node>, Double> congestionFactors;

    public enum CongestionLevel {
        LOW(1.2),
        MEDIUM(1.5),
        HIGH(1000.0);

        private double value;

        CongestionLevel(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public static CongestionLevel fromString(String level) {
            for (CongestionLevel cl : CongestionLevel.values()) {
                if (cl.name().equalsIgnoreCase(level)) {
                    return cl;
                }
            }
            throw new IllegalArgumentException("Invalid congestion level: " + level);
        }
    }

    public CongestionController() {
        congestionFactors = new HashMap<>();
    }

    public void setCongestionLevel(Node a, Node b, String level) {
        CongestionLevel congestionLevel = CongestionLevel.fromString(level);
        setCongestion(a, b, congestionLevel.getValue());
    }

    private void setCongestion(Node a, Node b, double factor) {
        congestionFactors.put(new AbstractMap.SimpleImmutableEntry<>(a, b), factor);
        congestionFactors.put(new AbstractMap.SimpleImmutableEntry<>(b, a), factor);
    }

    public double getCongestionFactor(Node a, Node b) {
        return congestionFactors.getOrDefault(new AbstractMap.SimpleImmutableEntry<>(a, b), 1.0);
    }

    public void printCongestionFactors() {
        System.out.println("Congestion Factors:");
        for (Map.Entry<AbstractMap.SimpleImmutableEntry<Node, Node>, Double> entry : congestionFactors.entrySet()) {
            Node nodeA = entry.getKey().getKey();
            Node nodeB = entry.getKey().getValue();
            double factor = entry.getValue();
            System.out.printf("Node A: %s, Node B: %s, Congestion Factor: %.2f%n", nodeA, nodeB, factor);
        }
    }
}
