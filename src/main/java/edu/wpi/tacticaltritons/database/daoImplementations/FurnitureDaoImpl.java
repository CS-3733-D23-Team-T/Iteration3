package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Furniture;
import edu.wpi.tacticaltritons.database.RequestStatus;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.FurnitureDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FurnitureDaoImpl implements FurnitureDao {
    @Override
    public Furniture get(int orderNum) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Furniture furniture = null;
        try {
            connection = Tdb.INSTANCE.getConnection();


            String sql = "SELECT * FROM FurnitureForms WHERE orderNum = ?;";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderNum);

            rs = ps.executeQuery();
            if (rs.next()) {
                int orderN = rs.getInt("orderNum");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String assignedStaffFirst = rs.getString("assignedStaffFirst");
                String assignedStaffLast = rs.getString("assignedStaffLast");
                Date deliveryDate = rs.getDate("date");
                String location = rs.getString("location");
                String items = rs.getString("items");
                RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

                furniture =
                        new Furniture(
                                orderN,
                                firstName,
                                lastName,
                                assignedStaffFirst,
                                assignedStaffLast,
                                deliveryDate,
                                location,
                                items,
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
        return furniture;
    }

    @Override
    public void update(Furniture furniture) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql =
                    "UPDATE FurnitureForms SET "
                            + "firstName = ?,"
                            + "lastName = ?,"
                            + "assignedStaffFirst = ?,"
                            + "assignedStaffLast = ?,"
                            + "date = ?,"
                            + "location = ?,"
                            + "items = ?,"
                            + "status = ? "
                            + "WHERE orderNum = ?;";

            ps = connection.prepareStatement(sql);

            ps.setInt(9, furniture.getOrderNum());
            ps.setString(1, furniture.getFirstName());
            ps.setString(2, furniture.getLastName());
            ps.setString(3, furniture.getAssignedStaffFirst());
            ps.setString(4, furniture.getAssignedStaffLast());
            ps.setDate(5, furniture.getDeliveryDate());
            ps.setString(6, furniture.getLocation());
            ps.setString(7, furniture.getItems());
            ps.setObject(8, furniture.getStatus(), Types.OTHER);

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
    public void insert(Furniture furniture) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql =
                    "INSERT INTO FurnitureForms (firstName, lastName, "
                            + "assignedStaffFirst, assignedStaffLast, date, location, items, status) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, furniture.getFirstName());
            ps.setString(2, furniture.getLastName());
            ps.setString(3, furniture.getAssignedStaffFirst());
            ps.setString(4, furniture.getAssignedStaffLast());
            ps.setDate(5,  furniture.getDeliveryDate());
            ps.setString(6, furniture.getLocation());
            ps.setString(7, furniture.getItems());
            ps.setObject(8, furniture.getStatus(), Types.OTHER);

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
    public void delete(Furniture furniture) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM FurnitureForms WHERE orderNum = ?";

            ps = connection.prepareStatement(sql);

            ps.setInt(1, furniture.getOrderNum());

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
    public List<Furniture> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Furniture> furnitures = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM FurnitureForms ORDER BY status, date;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                int orderNum = rs.getInt("orderNum");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String assignedStaffFirst = rs.getString("assignedStaffFirst");
                String assignedStaffLast = rs.getString("assignedStaffLast");
                Date deliveryDate = rs.getDate("date");
                String location = rs.getString("location");
                String items = rs.getString("items");
                RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

                Furniture furniture =
                        new Furniture(
                                orderNum,
                                firstName,
                                lastName,
                                assignedStaffFirst,
                                assignedStaffLast,
                                deliveryDate,
                                location,
                                items,
                                status);

                furnitures.add(furniture);
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
        return furnitures;
    }
}
