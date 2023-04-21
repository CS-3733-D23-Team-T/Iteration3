package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Furniture;
import edu.wpi.tacticaltritons.database.Invitations;
import edu.wpi.tacticaltritons.database.RequestStatus;
import edu.wpi.tacticaltritons.database.Tdb;
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
    public List<Invitations> getAll(String sessionFirstName, String sessionLastName) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Invitations> invitations = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT I.firstName, I.lastName, I.conferenceID, I.accepted, C.date, C.location " +
                    "FROM Invitations I " +
                    "JOIN Conference C ON C.orderNum = I.conferenceID " +
                    "WHERE I.firstName = ? AND I.lastName = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, sessionFirstName);
            ps.setString(2, sessionLastName);

            rs = ps.executeQuery(sql);

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
    public void delete(Invitations invitations) throws SQLException {

    }
}
