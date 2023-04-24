package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Meal;
import edu.wpi.tacticaltritons.database.RequestStatus;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.MealDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDaoImpl implements MealDao {

  @Override
  public void update(Meal meal) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "UPDATE Meal SET "
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

      ps.setInt(13, meal.getOrderNum());
      ps.setString(1, meal.getRequesterFirst());
      ps.setString(2, meal.getRequesterLast());
      ps.setString(3, meal.getPatientFirst());
      ps.setString(4, meal.getPatientLast());
      ps.setString(5, meal.getAssignedStaffFirst());
      ps.setString(6, meal.getAssignedStaffLast());
      ps.setDate(7, (Date) meal.getDeliveryDate());
      ps.setTime(8, meal.getDeliveryTime());
      ps.setString(9, meal.getLocation());
      ps.setString(10, meal.getItems());
      ps.setInt(11, meal.getTotal());
      ps.setObject(12, meal.getStatus(), Types.OTHER);

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
      if(connection != null){
        connection.close();
      }
    }
  }

  @Override
  public void insert(Meal meal) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql =
              "INSERT INTO Meal (requesterFirst, requesterLast, patientFirst, patientLast,"
                      + "assignedStaffFirst, assignedStaffLast, deliveryDate, deliveryTime, location, items, total, status) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      ps = connection.prepareStatement(sql);

      ps.setString(1, meal.getRequesterFirst());
      ps.setString(2, meal.getRequesterLast());
      ps.setString(3, meal.getPatientFirst());
      ps.setString(4, meal.getPatientLast());
      ps.setString(5, meal.getAssignedStaffFirst());
      ps.setString(6, meal.getAssignedStaffLast());
      ps.setDate(7, (Date) meal.getDeliveryDate());
      ps.setTime(8, meal.getDeliveryTime());
      ps.setString(9, meal.getLocation());
      ps.setString(10, meal.getItems());
      ps.setInt(11, meal.getTotal());
      ps.setObject(12, meal.getStatus(), Types.OTHER);

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
  public void delete(Meal meal) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getConnection();
      String sql = "DELETE FROM Meal WHERE orderNum = ?";

      ps = connection.prepareStatement(sql);

      ps.setInt(1, meal.getOrderNum());

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
  public Meal get(int orderNum) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Meal meal = null;
    try {
      connection = Tdb.getConnection();

      String sql = "SELECT * FROM Meal WHERE orderNum = ?;";
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

        meal =
                new Meal(
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
    return meal;
  }

  @Override
  public List<Meal> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Meal> meals = new ArrayList<>();
    try {
      connection = Tdb.getConnection();


      String sql = "SELECT * FROM Meal ORDER BY status, deliverydate, deliverytime;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
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

        Meal meal =
                new Meal(
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

        meals.add(meal);
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
    return meals;
  }
}
