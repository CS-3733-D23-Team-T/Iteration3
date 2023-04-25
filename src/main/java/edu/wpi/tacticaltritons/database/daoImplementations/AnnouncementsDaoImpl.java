package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Announcements;
import edu.wpi.tacticaltritons.database.Session;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.AnnouncementsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnnouncementsDaoImpl implements AnnouncementsDao {
    @Override
    public List<Announcements> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Announcements> announcements = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM announcements;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                UUID id = rs.getObject("id", UUID.class);
                String title = rs.getString("title");
                String content = rs.getString("content");
                String creator = rs.getString("creator");
                Timestamp creationDate = rs.getTimestamp("creationDate");
                Timestamp effectiveDate = rs.getTimestamp("effectiveDate");
                String type = rs.getString("type");
                String audience = rs.getString("audience");
                int urgency = rs.getInt("urgency");

                Announcements announcement = new Announcements(id,title,content,creator,creationDate,effectiveDate,type,audience,urgency);
                announcements.add(announcement);
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
        return announcements;
    }

    @Override
    public void insert(Announcements announcements) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "INSERT INTO announcements (id, " +
                    "title, " +
                    "content, " +
                    "creator, " +
                    "creationdate, " +
                    "effectivedate, " +
                    "type, " +
                    "audience, " +
                    "urgency) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setObject(1, announcements.getId());
            ps.setString(2, announcements.getTitle());
            ps.setString(3, announcements.getContent());
            ps.setString(4, announcements.getCreator());
            ps.setTimestamp(5, announcements.getCreationDate());
            ps.setTimestamp(6, announcements.getEffectiveDate());
            ps.setString(7, announcements.getType());
            ps.setObject(8, announcements.getAudience());
            ps.setInt(9, announcements.getUrgency());

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
    public void delete(Announcements announcements) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM announcements WHERE id = ?";

            ps = connection.prepareStatement(sql);

            ps.setObject(1, announcements.getId());

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
    public void update(Announcements announcements) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "UPDATE announcements SET " +
                    "title = ?, " +
                    "content = ?, " +
                    "creator = ?, " +
                    "creationdate = ?, " +
                    "effectivedate = ?, " +
                    "type = ?, " +
                    "audience = ?, " +
                    "urgency = ? " +
                    "WHERE id = ?";

            ps = connection.prepareStatement(sql);

            ps.setObject(9, announcements.getId());
            ps.setString(1, announcements.getTitle());
            ps.setString(2, announcements.getContent());
            ps.setString(3, announcements.getCreator());
            ps.setTimestamp(4, announcements.getCreationDate());
            ps.setTimestamp(5, announcements.getEffectiveDate());
            ps.setString(6, announcements.getType());
            ps.setObject(7, announcements.getAudience());
            ps.setInt(8, announcements.getUrgency());

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
    public Announcements get(UUID id) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Announcements announcement = null;
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM announcements WHERE id = ?;";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                String creator = rs.getString("creator");
                Timestamp creationDate = rs.getTimestamp("creationDate");
                Timestamp effectiveDate = rs.getTimestamp("effectiveDate");
                String type = rs.getString("type");
                String audience = rs.getString("audience");
                int urgency = rs.getInt("urgency");

                announcement = new Announcements(id,title,content,creator,creationDate,effectiveDate,type,audience,urgency);
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
        return announcement;
    }
}
