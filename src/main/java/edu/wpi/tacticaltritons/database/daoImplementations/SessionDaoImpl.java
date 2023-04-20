package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Session;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.SessionDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionDaoImpl implements SessionDao {

    @Override
    public List<Session> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Session> sessions = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM Sessions;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String username = rs.getString("username");
                Session session = mapResult(rs, username);
                sessions.add(session);
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
        return sessions;
    }

    private Session mapResult(ResultSet result, String username) throws SQLException {
        String location = result.getString("location");
        String firstName = result.getString("firstName");
        String lastName = result.getString("lastName");
        String email = result.getString("email");
        boolean admin = result.getBoolean("admin");
        int time = result.getInt("time");
        UUID tokenID = result.getObject("tokenid", UUID.class);
        boolean userTFA = result.getBoolean("userTFA");

        return new Session(username,
                location,
                firstName,
                lastName,
                email,
                admin,
                time,
                tokenID,
                userTFA);
    }

    @Override
    public void insert(Session session) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "INSERT INTO Sessions (username, " +
                    "location, " +
                    "firstName, " +
                    "lastName, " +
                    "email, " +
                    "admin, " +
                    "time, " +
                    "tokenid, " +
                    "usertfa) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);
            mapStatement(ps, session, true);

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

    private void mapStatement(PreparedStatement statement, Session session, boolean type) throws SQLException {
        statement.setString(type ? 1 : 9, session.getUsername());
        statement.setString(type ? 2 : 1, session.getLocation());
        statement.setString(type ? 3 : 2, session.getFirstname());
        statement.setString(type ? 4 : 3, session.getLastname());
        statement.setString(type ? 5 : 4, session.getEmail());
        statement.setBoolean(type ? 6 : 5, session.isAdmin());
        statement.setInt(type ? 7 : 6, session.getSessionTime());
        statement.setObject(type ? 8 : 7, session.getTokenId());
        statement.setBoolean(type ? 9 : 8, session.getUserTFA());
    }

    @Override
    public void delete(Session session) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM Sessions WHERE username = ?";

            ps = connection.prepareStatement(sql);

            ps.setString(1, session.getUsername());

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
    public Session get(String username) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Session session = null;
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM Sessions WHERE username = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            rs = ps.executeQuery();
            if (rs.next()) {
                session = mapResult(rs, username);
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
        return session;
    }
    @Override
    public void update(Session session) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "UPDATE Sessions SET " +
                    "location = ?, " +
                    "firstName = ?, " +
                    "lastName = ?, " +
                    "email = ?, " +
                    "admin = ?, " +
                    "time = ?, " +
                    "tokenid = ?, " +
                    "usertfa = ? " +
                    "WHERE username = ?";

            ps = connection.prepareStatement(sql);
            mapStatement(ps, session, false);

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
