package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Supply;
import edu.wpi.tacticaltritons.database.RequestStatus;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.SupplyDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyDaoImpl implements SupplyDao {

  @Override
  public Supply get(int orderNum) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Supply supply = null;
    try {
      connection = Tdb.INSTANCE.getConnection();


      String sql = "SELECT * FROM officesuppliesform WHERE orderNum = ?;";
      ps = connection.prepareStatement(sql);
      ps.setInt(1, orderNum);

      rs = ps.executeQuery();
      if (rs.next()) {
        int orderN = rs.getInt("orderNum");
        String requesterFirst = rs.getString("firstname");
        String requesterLast = rs.getString("lastname");
        String assignedStaffFirst = rs.getString("assignedStaffFirst");
        String assignedStaffLast = rs.getString("assignedStaffLast");
        Date deliveryDate = rs.getDate("date");
        Time deliveryTime = rs.getTime("time");
        String location = rs.getString("location");
        String items = rs.getString("items");
        int total = rs.getInt("price");
        RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

        supply =
                new Supply(
                        orderN,
                        requesterFirst,
                        requesterLast,
                        assignedStaffFirst,
                        assignedStaffLast,
                        deliveryDate,
                        deliveryTime,
                        location,
                        items,
                        total,
                        status);
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
    return supply;
  }

  @Override
  public void update(Supply supply) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "UPDATE officesuppliesform SET "
                      + "firstname = ?,"
                      + "lastname = ?,"
                      + "assignedStaffFirst = ?,"
                      + "assignedStaffLast = ?,"
                      + "date = ?,"
                      + "time = ?,"
                      + "location = ?,"
                      + "items = ?,"
                      + "price = ?,"
                      + "status = ? "
                      + "WHERE orderNum = ?;";

      ps = connection.prepareStatement(sql);

      ps.setInt(11, supply.getOrderNum());
      ps.setString(1, supply.getRequesterFirst());
      ps.setString(2, supply.getRequesterLast());
      ps.setString(3, supply.getAssignedStaffFirst());
      ps.setString(4, supply.getAssignedStaffLast());
      ps.setDate(5, supply.getDeliveryDate());
      ps.setTime(6, supply.getDeliveryTime());
      ps.setString(7, supply.getLocation());
      ps.setString(8, supply.getItems());
      ps.setInt(9, supply.getTotal());
      ps.setObject(10, supply.getStatus(), Types.OTHER);

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
      if(connection != null){
        connection.close();
      }
    }
  }

  @Override
  public void insert(Supply supply) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "INSERT INTO officesuppliesform (firstname, lastname, "
                      + "assignedStaffFirst, assignedStaffLast, date, time, location, items, price, status) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setString(1, supply.getRequesterFirst());
      ps.setString(2, supply.getRequesterLast());
      ps.setString(3, supply.getAssignedStaffFirst());
      ps.setString(4, supply.getAssignedStaffLast());
      ps.setDate(5, supply.getDeliveryDate());
      ps.setTime(6, supply.getDeliveryTime());
      ps.setString(7, supply.getLocation());
      ps.setString(8, supply.getItems());
      ps.setInt(9, supply.getTotal());
      ps.setObject(10, supply.getStatus(), Types.OTHER);

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
      if(connection != null){
        connection.close();
      }
    }
  }

  @Override
  public void delete(Supply supply) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "DELETE FROM officesuppliesform WHERE orderNum = ?";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, supply.getOrderNum());

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
      if(connection != null){
        connection.close();
      }
    }
  }

  @Override
  public List<Supply> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Supply> supplys = new ArrayList<>();
    try {
      connection = Tdb.getConnection();

      String sql = "SELECT * FROM officesuppliesform ORDER BY orderNum DESC;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        int orderNum = rs.getInt("orderNum");
        String requesterFirst = rs.getString("firstname");
        String requesterLast = rs.getString("lastname");
        String assignedStaffFirst = rs.getString("assignedStaffFirst");
        String assignedStaffLast = rs.getString("assignedStaffLast");
        Date deliveryDate = rs.getDate("date");
        Time deliveryTime = rs.getTime("time");
        String location = rs.getString("location");
        String items = rs.getString("items");
        int total = rs.getInt("price");
        RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

        Supply supply =
                new Supply(
                        orderNum,
                        requesterFirst,
                        requesterLast,
                        assignedStaffFirst,
                        assignedStaffLast,
                        deliveryDate,
                        deliveryTime,
                        location,
                        items,
                        total,
                        status);

        supplys.add(supply);
      }
    } catch (SQLException | ClassNotFoundException e){
      e.printStackTrace();
    } finally {
      if(rs != null){
        rs.close();
      }
      if(statement != null){
        statement.close();
      }
      if(connection != null){
        connection.close();
      }
    }
    return supplys;
  }
}
