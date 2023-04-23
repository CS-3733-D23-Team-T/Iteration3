package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Conference;
import edu.wpi.tacticaltritons.database.RequestStatus;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.ConferenceDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConferenceDaoImpl implements ConferenceDao {
    @Override
    public Conference get(int orderNum) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conference conference = null;
        try {
            connection = Tdb.getConnection();


            String sql = "SELECT * FROM Conference WHERE orderNum = ?;";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, orderNum);

            rs = ps.executeQuery();
            if (rs.next()) {
                int orderN = rs.getInt("orderNum");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Date date = rs.getDate("date");
                String attendance = rs.getString("attendance");
                int expectedSize = rs.getInt("expectedSize");
                String location = rs.getString("location");
                RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

                conference =
                        new Conference(
                                orderN,
                                firstName,
                                lastName,
                                date,
                                attendance,
                                expectedSize,
                                location,
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
        return conference;
    }

    @Override
    public void update(Conference conference) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql =
                    "UPDATE Conference SET "
                            + "firstName varchar(25), "
                            + "lastName varchar(25), "
                            + "Date date, "
                            + "attendance varchar(225), "
                            + "expectedSize numeric(5), "
                            + "location varchar(50), "
                            + "status varchar(50), "
                            + "WHERE orderNum = ?;";

            ps = connection.prepareStatement(sql);

            ps.setInt(8, conference.getOrderNum());
            ps.setString(1, conference.getFirstName());
            ps.setString(2, conference.getLastName());
            ps.setDate(3, conference.getDate());
            ps.setString(4, conference.getAttendance());
            ps.setInt(5, conference.getExpectedSize());
            ps.setString(6, conference.getLocation());
            ps.setObject(7, conference.getStatus(), Types.OTHER);

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
    public void insert(Conference conference) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql =
                    "INSERT INTO Conference (firstName, lastName, " +
                            "date, attendance, expectedSize, location, status) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, conference.getFirstName());
            ps.setString(2, conference.getLastName());
            ps.setDate(3, conference.getDate());
            ps.setString(4, conference.getAttendance());
            ps.setInt(5, conference.getExpectedSize());
            ps.setString(6, conference.getLocation());
            ps.setObject(7, conference.getStatus(), Types.OTHER);

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
    public void delete(Conference conference) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM Conference WHERE orderNum = ?";

            ps = connection.prepareStatement(sql);

            ps.setInt(1, conference.getOrderNum());

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
    public List<Conference> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Conference> conferences = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM Conference ORDER BY status, date;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                int orderN = rs.getInt("orderNum");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Date date = rs.getDate("date");
                String attendance = rs.getString("attendance");
                int expectedSize = rs.getInt("expectedSize");
                String location = rs.getString("location");
                RequestStatus status = RequestStatus.getEnum(rs.getString("status"));

                Conference conference =
                        new Conference(
                                orderN,
                                firstName,
                                lastName,
                                date,
                                attendance,
                                expectedSize,
                                location,
                                status);

                conferences.add(conference);
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
            if(connection != null){
                connection.close();
            }
        }
        return conferences;
    }
}
