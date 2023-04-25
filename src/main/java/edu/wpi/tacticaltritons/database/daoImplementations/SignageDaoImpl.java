package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.FurnitureRequestOptions;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Signage;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.SignageDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SignageDaoImpl implements SignageDao {
    @Override
    public List<Signage> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Signage> signs = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM signage;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                Array forwarddirArray = rs.getArray("forwarddir");
                Array leftdirArray = rs.getArray("leftdir");
                Array rightdirArray = rs.getArray("rightdir");
                Array backdirArray = rs.getArray("backdir");

                String title = rs.getString("title");
                String[] forwarddir = (String[]) forwarddirArray.getArray();
                String[] leftdir = (String[]) leftdirArray.getArray();
                String[] rightdir = (String[]) rightdirArray.getArray();
                String[] backdir = (String[]) backdirArray.getArray();
                boolean singleDisplay = rs.getBoolean("singleDisplay");

                Signage sign = new Signage(title, forwarddir, leftdir, rightdir, backdir, singleDisplay);

                signs.add(sign);
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
        return signs;
    }

    @Override
    public void insert(Signage sign) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "INSERT INTO signage (title, forwarddir, leftdir, rightdir, backdir, singleDisplay) VALUES (?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, sign.getTitle());
            ps.setArray(2, connection.createArrayOf("VARCHAR", sign.getForwarddir()));
            ps.setArray(3, connection.createArrayOf("VARCHAR", sign.getLeftdir()));
            ps.setArray(4, connection.createArrayOf("VARCHAR", sign.getRightdir()));
            ps.setArray(5, connection.createArrayOf("VARCHAR", sign.getBackdir()));
            ps.setBoolean(6, sign.isSingleDisplay());

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
    public void delete(Signage signage) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM signage WHERE title = ?";

            ps = connection.prepareStatement(sql);

            ps.setString(1, signage.getTitle());

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
    public void update(Signage sign) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "UPDATE signage SET forwarddir = ?, leftdir = ?, rightdir = ?, backdir = ?, singleDisplay = ? WHERE title = ?;";

            ps = connection.prepareStatement(sql);

            ps.setString(6, sign.getTitle());
            ps.setArray(1, connection.createArrayOf("VARCHAR", sign.getForwarddir()));
            ps.setArray(2, connection.createArrayOf("VARCHAR", sign.getLeftdir()));
            ps.setArray(3, connection.createArrayOf("VARCHAR", sign.getRightdir()));
            ps.setArray(4, connection.createArrayOf("VARCHAR", sign.getBackdir()));
            ps.setBoolean(5, sign.isSingleDisplay());

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
    public Signage get(String title) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Signage sign = null;
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM signage WHERE title = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, title);

            rs = ps.executeQuery();
            if (rs.next()) {
                Array forwarddirArray = rs.getArray("forwarddir");
                Array leftdirArray = rs.getArray("leftdir");
                Array rightdirArray = rs.getArray("rightdir");
                Array backdirArray = rs.getArray("backdir");

                String[] forwarddir = (String[]) forwarddirArray.getArray();
                String[] leftdir = (String[]) leftdirArray.getArray();
                String[] rightdir = (String[]) rightdirArray.getArray();
                String[] backdir = (String[]) backdirArray.getArray();
                boolean singleDisplay = rs.getBoolean("singleDisplay");

                sign = new Signage(title, forwarddir, leftdir, rightdir, backdir, singleDisplay);
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
        return sign;
    }
}
