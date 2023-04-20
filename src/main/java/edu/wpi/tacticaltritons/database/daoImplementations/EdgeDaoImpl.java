package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Edge;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.EdgeDao;
import oracle.ucp.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EdgeDaoImpl implements EdgeDao {

  @Override
  public Edge get(Node startNode, Node endNode) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Edge edge = null;
    try {
      connection = Tdb.getConnection();
      String sql = "SELECT * FROM Edge WHERE startNode = ? AND endNode = ?;";
      ps = connection.prepareStatement(sql);
      ps.setInt(1, startNode.getNodeID());
      ps.setInt(2, endNode.getNodeID());

      rs = ps.executeQuery();
      if (rs.next()) {
        int congestion = rs.getInt("congestion");

        edge = new Edge(startNode, endNode, congestion);
      }
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(rs != null){
        rs.close();
      }
      if(ps != null){
        ps.close();
      }
    }
    return edge;
  }

  @Override
  public List<Edge> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Edge> edges = new ArrayList<>();
    try {
      connection = Tdb.getConnection();

      String sql = "SELECT e.startNode, e.endNode, e.congestion, n1.nodeID AS startNodeID, n1.xcoord AS startXcoord, n1.ycoord AS startYcoord, n1.floor AS startFloor, n1.building AS startBuilding, n2.nodeID AS endNodeID, n2.xcoord AS endXcoord, n2.ycoord AS endYcoord, n2.floor AS endFloor, n2.building AS endBuilding FROM Edge e JOIN Node n1 ON e.startNode = n1.nodeID JOIN Node n2 ON e.endNode = n2.nodeID";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        int startNodeID = rs.getInt("startNodeID");
        int endNodeID = rs.getInt("endNodeID");
        int congestion = rs.getInt("congestion");
        int startXcoord = rs.getInt("startXcoord");
        int startYcoord = rs.getInt("startYcoord");
        String startFloor = rs.getString("startFloor");
        String startBuilding = rs.getString("startBuilding");
        int endXcoord = rs.getInt("endXcoord");
        int endYcoord = rs.getInt("endYcoord");
        String endFloor = rs.getString("endFloor");
        String endBuilding = rs.getString("endBuilding");

        Node startNode = new Node(startNodeID, startXcoord, startYcoord, startFloor, startBuilding);
        Node endNode = new Node(endNodeID, endXcoord, endYcoord, endFloor, endBuilding);
        Edge edge = new Edge(startNode, endNode, congestion);

        edges.add(edge);
      }
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(rs != null){
        rs.close();
      }
      if(statement != null){
        statement.close();
      }
    }

    return edges;
  }

  @Override
  public void insert(Edge edge) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "INSERT INTO Edge (startNode, endNode, congestion) VALUES (?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, edge.getStartNode().getNodeID());
      ps.setInt(2, edge.getEndNode().getNodeID());
      ps.setInt(3, edge.getCongestion());

      int result = ps.executeUpdate();
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(rs != null){
        rs.close();
      }
      if(ps != null){
        ps.close();
      }
    }
  }

  @Override
  public void delete(Edge edge) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "DELETE FROM Edge WHERE (startNode = ? AND endNode = ?)";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, edge.getStartNode().getNodeID());
      ps.setInt(2, edge.getEndNode().getNodeID());

      int result = ps.executeUpdate();
    } catch (SQLException e){
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
        if(rs != null){
          rs.close();
        }
        if(ps != null){
          ps.close();
        }
      }
  }
}
