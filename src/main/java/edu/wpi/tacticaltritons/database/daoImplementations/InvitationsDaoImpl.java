package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.dao.InvitationsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitationsDaoImpl implements InvitationsDao {
    @Override
    public List<Invitations> getAll() throws SQLException {
        return null;
    }

    @Override
    public List<Invitations> getAll(String sessionFirstName, String sessionLastName, Date currentDate) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Invitations> invitations = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT I.firstName, I.lastName, I.conferenceID, I.accepted, C.date, C.location " +
                    "FROM Invitations I " +
                    "JOIN Conference C ON C.orderNum = I.conferenceID " +
                    "WHERE I.firstName = ? AND I.lastName = ? AND C.date >= ? " +
                    "ORDER BY C.date;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, sessionFirstName);
            ps.setString(2, sessionLastName);
            ps.setDate(3,currentDate);

            rs = ps.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int conferenceID = rs.getInt("conferenceID");
                boolean accepted = rs.getBoolean("accepted");
                Date date = rs.getDate("date");
                String location = rs.getString("location");

                Invitations invite = new Invitations(conferenceID,firstName,lastName,accepted,date,location);

                invitations.add(invite);
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
        return invitations;
    }

    @Override
    public void insert(Invitations invitations) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql =
                    "INSERT INTO Invitations (firstName, lastName, "
                            + "conferenceID, accepted, date, location) "
                            + "VALUES (?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, invitations.getFirstName());
            ps.setString(2, invitations.getLastName());
            ps.setInt(3,invitations.getConferenceID());
            ps.setBoolean(4, invitations.isAccepted());
            ps.setDate(5, invitations.getDate());
            ps.setString(6, invitations.getLocation());

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
    public void update(Invitations invitation) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql =
                    "UPDATE Invitations SET accepted = ? where conferenceID = ? AND firstName = ? ANd lastName = ?";

            ps = connection.prepareStatement(sql);

            ps.setBoolean(1, invitation.isAccepted());
            ps.setInt(2, invitation.getConferenceID());
            ps.setString(3, invitation.getFirstName());
            ps.setString(4, invitation.getLastName());

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
    public void delete(Invitations invitations) throws SQLException {

    }
}
