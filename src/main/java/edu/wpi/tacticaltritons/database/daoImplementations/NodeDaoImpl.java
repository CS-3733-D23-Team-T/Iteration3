package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.NodeDao;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class NodeDaoImpl implements NodeDao {

  @Override
  public Node get(int nodeID) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Node node = null;
    try {
      connection = Tdb.getConnection();

      String sql = "SELECT * FROM Node WHERE nodeID = ?;";
      ps = connection.prepareStatement(sql);
      ps.setInt(1, nodeID);

      rs = ps.executeQuery();
      if (rs.next()) {
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        node = new Node(nodeID, xcoord, ycoord, floor, building);
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
    return node;
  }

  @Override
  public Node get(String locationName, Date date) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Node node = null;
    try {
      connection = Tdb.getConnection();

      String sql =
              "SELECT Node.*"
                      + "FROM Node "
                      + "JOIN Move ON Node.nodeid = Move.nodeid "
                      + "JOIN LocationName ON LocationName.longName = Move.longname "
                      + "WHERE LocationName.longName = ? AND Move.date <= ?;";
      ps = connection.prepareStatement(sql);
      ps.setString(1, locationName);
      ps.setDate(2, new java.sql.Date(date.getTime()));

      rs = ps.executeQuery();
      if (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        node = new Node(nodeID, xcoord, ycoord, floor, building);
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
    return node;
  }

  @Override
  public List<Node> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Node> nodes = new ArrayList<>();
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT * FROM Node;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);

        nodes.add(node);
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
    return nodes;
  }

  @Override
  public void insert(Node node) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "INSERT INTO Node (nodeID, xcoord, ycoord, floor, building) VALUES (?, ?, ?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, node.getNodeID());
      ps.setInt(2, node.getXcoord());
      ps.setInt(3, node.getYcoord());
      ps.setString(4, node.getFloor());
      ps.setString(5, node.getBuilding());

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
  public void update(Node node) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "UPDATE Node SET xcoord = ?, ycoord = ?, floor = ?, building = ? where nodeID = ?";

      ps = connection.prepareStatement(sql);

      ps.setInt(5, node.getNodeID());
      ps.setInt(1, node.getXcoord());
      ps.setInt(2, node.getYcoord());
      ps.setString(3, node.getFloor());
      ps.setString(4, node.getBuilding());

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
  public void update(Node oldNode, Node newNode) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "UPDATE Node SET nodeID = ?, xcoord = ?, ycoord = ?, floor = ?, building = ? " +
                      "where nodeID = ?";

      ps = connection.prepareStatement(sql);

      ps.setInt(6, oldNode.getNodeID());
      ps.setInt(1, newNode.getNodeID());
      ps.setInt(2, newNode.getXcoord());
      ps.setInt(3, newNode.getYcoord());
      ps.setString(4, newNode.getFloor());
      ps.setString(5, newNode.getBuilding());

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
  public void delete(Node node) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "DELETE FROM Node WHERE nodeID = ?";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, node.getNodeID());

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
  public HashMap<Integer, ArrayList<Node>> getAllNeighbors() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    HashMap<Integer, ArrayList<Node>> hash = new HashMap<>();
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT n.nodeid, " +
              "       string_agg(CAST(e.endnode AS TEXT), ',') AS neighbors, " +
              "       array_agg(n2.xcoord) AS xcoords, " +
              "       array_agg(n2.ycoord) AS ycoords, " +
              "       array_agg(n2.floor) AS floors, " +
              "       array_agg(n2.building) AS buildings " +
              "FROM node n " +
              "         JOIN edge e ON n.nodeid = e.startnode " +
              "         JOIN node n2 ON e.endnode = n2.nodeid " +
              "GROUP BY n.nodeid;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String[] neighbors = rs.getString("neighbors").split(",");
        String[] xcoords = rs.getString("xcoords").split(",");
        String[] ycoords = rs.getString("ycoords").split(",");
        String[] floors = rs.getString("floors").split(",");
        String[] buildings = rs.getString("buildings").split(",");
        ArrayList<Node> nodes = new ArrayList<>();
        for(int i = 0; i<neighbors.length; i++){
            Node node = new Node(Integer.parseInt(neighbors[i]), Integer.parseInt(xcoords[i]), Integer.parseInt(ycoords[i]), floors[i], buildings[i]);
            nodes.add(node);
        }

        hash.put(nodeID, nodes);
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
    return hash;
  }
}
