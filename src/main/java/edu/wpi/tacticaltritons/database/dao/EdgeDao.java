package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;

public interface EdgeDao extends IDao<Edge> {
  Edge get(Node startNode, Node endNode) throws SQLException;
}
