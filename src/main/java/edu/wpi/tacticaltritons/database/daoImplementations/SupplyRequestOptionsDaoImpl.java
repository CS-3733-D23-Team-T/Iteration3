package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.FlowerRequestOptions;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.FlowerRequestOptionsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyRequestOptionsDaoImpl implements FlowerRequestOptionsDao {
    @Override
    public FlowerRequestOptions get(String itemName, String shop) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        FlowerRequestOptions flowerRequestOptions = null;
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM FlowerRequestOptions WHERE itemName = ? AND shop  = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, itemName);
            ps.setString(2, shop);

            rs = ps.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");
                flowerRequestOptions = new FlowerRequestOptions(itemName, price, shop, shopDescription, itemType, itemDescription);
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
        return flowerRequestOptions;
    }
    @Override
    public List<FlowerRequestOptions> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<FlowerRequestOptions> flowerRequestOptions = new ArrayList<>();

        try {
            connection = Tdb.getConnection();
            String sql = "SELECT * FROM FlowerRequestOptions;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String itemName = rs.getString("itemName");
                double prices = rs.getDouble("prices");
                String shop = rs.getString("shop");
                String shopDescription = rs.getString("shopDescription");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");

                FlowerRequestOptions option = new FlowerRequestOptions(itemName, prices, shop, shopDescription, itemType, itemDescription);

                flowerRequestOptions.add(option);
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
        return flowerRequestOptions;
    }

    @Override
    public void insert(FlowerRequestOptions flowerRequestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "INSERT INTO FlowerRequestOptions (itemName, prices, shop, shopDescription, itemType, itemDescription) VALUES (?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, flowerRequestOptions.getItemName());
            ps.setDouble(2, flowerRequestOptions.getPrice());
            ps.setString(3, flowerRequestOptions.getShop());
            ps.setString(4, flowerRequestOptions.getShopDescription());
            ps.setString(5, flowerRequestOptions.getItemType());
            ps.setString(6, flowerRequestOptions.getItemDescription());

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
    public void delete(FlowerRequestOptions flowerRequestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM FlowerRequestOptions WHERE (itemName = ? AND shop = ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, flowerRequestOptions.getItemName());
            ps.setString(2, flowerRequestOptions.getShop());

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
    public List<FlowerRequestOptions> getFromShop(String shop) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<FlowerRequestOptions> flowerRequestOptions = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM FlowerRequestOptions WHERE shop = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, shop);

            rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("itemName");
                double prices = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");

                FlowerRequestOptions option = new FlowerRequestOptions(itemName, prices, shop, shopDescription, itemType, itemDescription);

                flowerRequestOptions.add(option);
            }
        }
        catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
        }
        return flowerRequestOptions;
    }

    @Override
    public void updatePrice(FlowerRequestOptions shopOption) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "UPDATE FlowerRequestOptions SET prices = ? where (itemName = ? AND shop = ?)";

            ps = connection.prepareStatement(sql);

            ps.setDouble(1, shopOption.getPrice());
            ps.setString(2, shopOption.getItemName());
            ps.setString(3, shopOption.getShop());

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
