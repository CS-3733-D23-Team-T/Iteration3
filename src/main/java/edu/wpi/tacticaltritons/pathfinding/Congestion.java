package edu.wpi.tacticaltritons.pathfinding;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
public class Congestion {
    private final int congestionLevel;


    public Congestion(int congestionLevel) {
        this.congestionLevel = congestionLevel;
    }
    //if query between nodes

    public void setCongestion(Node startNode, Node endNode, int congestionLevel){


    }
}
