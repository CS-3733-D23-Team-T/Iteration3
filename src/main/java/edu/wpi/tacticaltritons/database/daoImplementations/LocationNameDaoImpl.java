package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.LocationNameDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationNameDaoImpl implements LocationNameDao {

  @Override
  public LocationName get(String locationName) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    LocationName location = null;
    try {
      connection = Tdb.getInstance().getConnection();

      String sql = "SELECT * FROM LocationName WHERE longName = ?;";
      ps = connection.prepareStatement(sql);
      ps.setString(1, locationName);

      rs = ps.executeQuery();
      if (rs.next()) {
        String shortName = rs.getString("shortName");
        String nodeType = rs.getString("nodeType");

        location = new LocationName(locationName, shortName, nodeType);
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
    return location;
  }

  @Override
  public List<LocationName> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<LocationName> locations = new ArrayList<>();
    try {
      connection = Tdb.getInstance().getConnection();

      String sql = "SELECT * FROM LocationName ORDER BY longName ASC;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        String longName = rs.getString("longName");
        String shortName = rs.getString("shortName");
        String nodeType = rs.getString("nodeType");

        LocationName location = new LocationName(longName, shortName, nodeType);

        locations.add(location);
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
    return locations;
  }

  @Override
  public void insert(LocationName locationName) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql = "INSERT INTO LocationName (longName, shortName, nodeType) VALUES (?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setString(1, locationName.getLongName());
      ps.setString(2, locationName.getShortName());
      ps.setString(3, locationName.getNodeType());

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
  public void update(LocationName locationName) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql = "UPDATE LocationName SET shortName = ?, nodeType = ? WHERE longName = ?";

      ps = connection.prepareStatement(sql);

      ps.setString(3, locationName.getLongName());
      ps.setString(1, locationName.getShortName());
      ps.setString(2, locationName.getNodeType());

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
  public void update(LocationName oldLocationName, LocationName newLocationName) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql = "UPDATE LocationName SET longName = ?, shortName = ?, nodeType = ? WHERE longName = ?";

      ps = connection.prepareStatement(sql);

      ps.setString(4, oldLocationName.getLongName());
      ps.setString(1, newLocationName.getLongName());
      ps.setString(2, newLocationName.getShortName());
      ps.setString(3, newLocationName.getNodeType());

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
  public void delete(LocationName locationName) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql = "DELETE FROM LocationName WHERE longName = ?";

      ps = connection.prepareStatement(sql);

      ps.setString(1, locationName.getLongName());

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
