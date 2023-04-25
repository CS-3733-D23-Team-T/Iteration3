package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.dao.MoveDao;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class MoveDaoImpl implements MoveDao {

  @Override
  public Move get(Node node, LocationName location, Date moveDate) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Move move = null;
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT * FROM Move WHERE nodeid = ? AND longName  = ? AND date = ?;";
      ps = connection.prepareStatement(sql);
      ps.setInt(1, node.getNodeID());
      ps.setString(2, location.getLongName());
      ps.setDate(3, moveDate);

      rs = ps.executeQuery();
      if (rs.next()) {
        move = new Move(node, location, moveDate);
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
      if(connection != null){
        connection.close();
      }
    }
    return move;
  }

  @Override
  public List<Move> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Move> moves = new ArrayList<>();
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT m.nodeID, m.longName, m.date, l.shortName, l.nodeType, n.xcoord, n.ycoord, n.floor, n.building "
              + "FROM Move m "
              + "JOIN Node n ON m.nodeID = n.nodeID "
              + "JOIN LocationName l ON m.longName = l.longName "
              + "ORDER BY m.date DESC;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        Date date = rs.getDate("date");
        String shortName = rs.getString("shortName");
        String nodeType = rs.getString("nodeType");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        LocationName locationName = new LocationName(longName, shortName, nodeType);
        Move move = new Move(node, locationName, date);
        moves.add(move);
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
    return moves;
  }

  @Override
  public List<Move> getAllCurrent(Date currentDate) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Move> moves = new ArrayList<>();
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT m.nodeID, m.longName, m.date, l.shortName, l.nodeType, n.xcoord, n.ycoord, n.floor, n.building "
              + "FROM Move m "
              + "JOIN Node n ON m.nodeID = n.nodeID "
              + "JOIN LocationName l ON m.longName = l.longName "
              + "WHERE m.date <= ? "
              + "ORDER BY m.date;";
      ps = connection.prepareStatement(sql);

      ps.setDate(1, currentDate);

      rs = ps.executeQuery();

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        Date date = rs.getDate("date");
        String shortName = rs.getString("shortName");
        String nodeType = rs.getString("nodeType");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        LocationName locationName = new LocationName(longName, shortName, nodeType);
        Move move = new Move(node, locationName, date);
        moves.add(move);
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
    return moves;
  }

  @Override
  public List<Move> getAllFuture(Date currentDate) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<Move> moves = new ArrayList<>();
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT m.nodeID, m.longName, m.date, l.shortName, l.nodeType, n.xcoord, n.ycoord, n.floor, n.building "
              + "FROM Move m "
              + "JOIN Node n ON m.nodeID = n.nodeID "
              + "JOIN LocationName l ON m.longName = l.longName "
              + "WHERE m.date > ? "
              + "ORDER BY m.date;";
      ps = connection.prepareStatement(sql);

      ps.setDate(1, currentDate);

      rs = ps.executeQuery();

      while (rs.next()) {
        int nodeID = rs.getInt("nodeID");
        String longName = rs.getString("longName");
        Date date = rs.getDate("date");
        String shortName = rs.getString("shortName");
        String nodeType = rs.getString("nodeType");
        int xcoord = rs.getInt("xcoord");
        int ycoord = rs.getInt("ycoord");
        String floor = rs.getString("floor");
        String building = rs.getString("building");

        Node node = new Node(nodeID, xcoord, ycoord, floor, building);
        LocationName locationName = new LocationName(longName, shortName, nodeType);
        Move move = new Move(node, locationName, date);
        moves.add(move);
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
    return moves;
  }

  @Override
  public void insert(Move move) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "INSERT INTO Move (nodeID, longName, date) VALUES (?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, move.getNode().getNodeID());
      ps.setString(2, move.getLocation().getLongName());
      ps.setDate(3, new java.sql.Date(move.getMoveDate().getTime()));

      int result = ps.executeUpdate();
    } catch (SQLException | ClassNotFoundException e){
      e.printStackTrace();
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
  public void delete(Move move) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "DELETE FROM Move WHERE (nodeID = ? AND longName = ? AND date = ?)";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, move.getNode().getNodeID());
      ps.setString(2, move.getLocation().getLongName());
      ps.setDate(3, (java.sql.Date) move.getMoveDate());

      int result = ps.executeUpdate();
    } catch (SQLException | ClassNotFoundException e){
      e.printStackTrace();
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
