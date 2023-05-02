package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.FurnitureRequestOptions;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.FurnitureRequestOptionsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FurnitureRequestOptionsDaoImpl implements FurnitureRequestOptionsDao {
    @Override
    public FurnitureRequestOptions get(String itemName) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FurnitureRequestOptions furnitureRequestOptions = null;
        try {
            connection = Tdb.getInstance().getConnection();

            String sql = "SELECT * FROM FurnitureRequestOptions WHERE itemName = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, itemName);

            rs = ps.executeQuery();
            if (rs.next()) {
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");
                furnitureRequestOptions = new FurnitureRequestOptions(itemName, itemType, itemDescription);
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
        return furnitureRequestOptions;
    }
    @Override
    public List<FurnitureRequestOptions> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<FurnitureRequestOptions> furnitureRequestOptions = new ArrayList<>();

        try {
            connection = Tdb.getInstance().getConnection();
            String sql = "SELECT * FROM FurnitureRequestOptions;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String itemName = rs.getString("itemName");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");

                FurnitureRequestOptions option = new FurnitureRequestOptions(itemName, itemType, itemDescription);

                furnitureRequestOptions.add(option);
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
        return furnitureRequestOptions;
    }

    @Override
    public void insert(FurnitureRequestOptions furnitureRequestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getInstance().getConnection();
            String sql = "INSERT INTO FurnitureRequestOptions (itemName, itemType, itemDescription) VALUES (?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, furnitureRequestOptions.getItemName());
            ps.setString(2, furnitureRequestOptions.getItemType());
            ps.setString(3, furnitureRequestOptions.getItemDescription());

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
    public void delete(FurnitureRequestOptions furnitureRequestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getInstance().getConnection();
            String sql = "DELETE FROM FurnitureRequestOptions WHERE (itemName = ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, furnitureRequestOptions.getItemName());

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

}
