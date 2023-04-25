package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public interface NodeDao extends IDao<Node> {
  Node get(int nodeID) throws SQLException;

  Node get(String locationName, Date date) throws SQLException;

  void update(Node node) throws SQLException;

    void update(Node oldNode, Node newNode) throws SQLException;

    HashMap<Integer, ArrayList<Node>> getAllNeighbors() throws SQLException;
}
