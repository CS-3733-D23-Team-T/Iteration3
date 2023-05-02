package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Flower;
import edu.wpi.tacticaltritons.database.RequestStatus;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.FlowerDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlowerDaoImpl implements FlowerDao {

  @Override
  public Flower get(int orderNum) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Flower flower = null;
    try {
      connection = Tdb.getInstance().getConnection();


      String sql = "SELECT * FROM Flower WHERE orderNum = ?;";
      ps = connection.prepareStatement(sql);
      ps.setInt(1, orderNum);

      rs = ps.executeQuery();
      if (rs.next()) {
        int orderN = rs.getInt("orderNum");
        String requesterFirst = rs.getString("requesterFirst");
        String requesterLast = rs.getString("requesterLast");
        String patientFirst = rs.getString("patientFirst");
        String patientLast = rs.getString("patientLast");
        String assignedStaffFirst = rs.getString("assignedStaffFirst");
        String assignedStaffLast = rs.getString("assignedStaffLast");
        Date deliveryDate = rs.getDate("deliveryDate");
        Time deliveryTime = rs.getTime("deliveryTime");
        String location = rs.getString("location");
        String items = rs.getString("items");
        int total = rs.getInt("total");
        RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

        flower =
                new Flower(
                        orderN,
                        requesterFirst,
                        requesterLast,
                        patientFirst,
                        patientLast,
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
    return flower;
  }

  @Override
  public void update(Flower flower) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql =
              "UPDATE Flower SET "
                      + "requesterFirst = ?,"
                      + "requesterLast = ?,"
                      + "patientFirst = ?,"
                      + "patientLast = ?,"
                      + "assignedStaffFirst = ?,"
                      + "assignedStaffLast = ?,"
                      + "deliveryDate = ?,"
                      + "deliveryTime = ?,"
                      + "location = ?,"
                      + "items = ?,"
                      + "total = ?,"
                      + "status = ? "
                      + "WHERE orderNum = ?;";

      ps = connection.prepareStatement(sql);

      ps.setInt(13, flower.getOrderNum());
      ps.setString(1, flower.getRequesterFirst());
      ps.setString(2, flower.getRequesterLast());
      ps.setString(3, flower.getPatientFirst());
      ps.setString(4, flower.getPatientLast());
      ps.setString(5, flower.getAssignedStaffFirst());
      ps.setString(6, flower.getAssignedStaffLast());
      ps.setDate(7, (java.sql.Date) flower.getDeliveryDate());
      ps.setTime(8, flower.getDeliveryTime());
      ps.setString(9, flower.getLocation());
      ps.setString(10, flower.getItems());
      ps.setInt(11, flower.getTotal());
      ps.setObject(12, flower.getStatus(), Types.OTHER);

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
  public void insert(Flower flower) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql =
              "INSERT INTO Flower (requesterFirst, requesterLast, patientFirst, patientLast,"
                      + "assignedStaffFirst, assignedStaffLast, deliveryDate, deliveryTime, location, items, total, status) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setString(1, flower.getRequesterFirst());
      ps.setString(2, flower.getRequesterLast());
      ps.setString(3, flower.getPatientFirst());
      ps.setString(4, flower.getPatientLast());
      ps.setString(5, flower.getAssignedStaffFirst());
      ps.setString(6, flower.getAssignedStaffLast());
      ps.setDate(7, (java.sql.Date) flower.getDeliveryDate());
      ps.setTime(8, flower.getDeliveryTime());
      ps.setString(9, flower.getLocation());
      ps.setString(10, flower.getItems());
      ps.setInt(11, flower.getTotal());
      ps.setObject(12, flower.getStatus(), Types.OTHER);

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
  public void delete(Flower flower) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql = "DELETE FROM Flower WHERE orderNum = ?";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, flower.getOrderNum());

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
  public List<Flower> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Flower> flowers = new ArrayList<>();
    try {
      connection = Tdb.getInstance().getConnection();

      String sql = "SELECT * FROM Flower ORDER BY status, deliverydate, deliverytime;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        int orderNum = rs.getInt("orderNum");
        String requesterFirst = rs.getString("requesterFirst");
        String requesterLast = rs.getString("requesterLast");
        String patientFirst = rs.getString("patientFirst");
        String patientLast = rs.getString("patientLast");
        String assignedStaffFirst = rs.getString("assignedStaffFirst");
        String assignedStaffLast = rs.getString("assignedStaffLast");
        Date deliveryDate = rs.getDate("deliveryDate");
        Time deliveryTime = rs.getTime("deliveryTime");
        String location = rs.getString("location");
        String items = rs.getString("items");
        int total = rs.getInt("total");
        RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

        Flower flower =
                new Flower(
                        orderNum,
                        requesterFirst,
                        requesterLast,
                        patientFirst,
                        patientLast,
                        assignedStaffFirst,
                        assignedStaffLast,
                        deliveryDate,
                        deliveryTime,
                        location,
                        items,
                        total,
                        status);

        flowers.add(flower);
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
    return flowers;
  }
}
