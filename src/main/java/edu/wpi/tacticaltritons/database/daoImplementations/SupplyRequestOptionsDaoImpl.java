package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.SupplyRequestOptions;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.SupplyRequestOptionsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyRequestOptionsDaoImpl implements SupplyRequestOptionsDao {
    @Override
    public SupplyRequestOptions get(String itemName, String shop) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        SupplyRequestOptions supplyRequestOptions = null;
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM SupplyRequestOptions WHERE itemName = ? AND shop  = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, itemName);
            ps.setString(2, shop);

            rs = ps.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");
                supplyRequestOptions = new SupplyRequestOptions(itemName, price, shop, itemType, itemDescription);
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
        return supplyRequestOptions;
    }
    @Override
    public List<SupplyRequestOptions> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<SupplyRequestOptions> supplyRequestOptions = new ArrayList<>();

        try {
            connection = Tdb.getConnection();
            String sql = "SELECT * FROM SupplyRequestOptions;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String itemName = rs.getString("itemName");
                double prices = rs.getDouble("prices");
                String shop = rs.getString("shop");
                String shopDescription = rs.getString("shopDescription");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");

                SupplyRequestOptions option = new SupplyRequestOptions(itemName, prices, shop, itemType, itemDescription);

                supplyRequestOptions.add(option);
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
        return supplyRequestOptions;
    }

    @Override
    public void insert(SupplyRequestOptions supplyRequestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "INSERT INTO SupplyRequestOptions (itemName, prices, shop, itemType, itemDescription) VALUES (?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, supplyRequestOptions.getItemName());
            ps.setDouble(2, supplyRequestOptions.getPrice());
            ps.setString(3, supplyRequestOptions.getShop());
            ps.setString(4, supplyRequestOptions.getShopDescription());
            ps.setString(5, supplyRequestOptions.getItemType());
            ps.setString(6, supplyRequestOptions.getItemDescription());

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
    public void delete(SupplyRequestOptions supplyRequestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "DELETE FROM SupplyRequestOptions WHERE (itemName = ? AND shop = ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, supplyRequestOptions.getItemName());
            ps.setString(2, supplyRequestOptions.getShop());

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
    public List<SupplyRequestOptions> getFromShop(String shop) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SupplyRequestOptions> supplyRequestOptions = new ArrayList<>();
        try {
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM SupplyRequestOptions WHERE shop = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, shop);

            rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("itemName");
                double prices = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemType = rs.getString("itemType");
                String itemDescription = rs.getString("itemDescription");

                SupplyRequestOptions option = new SupplyRequestOptions(itemName, prices, shop, itemType, itemDescription);

                supplyRequestOptions.add(option);
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
        return supplyRequestOptions;
    }

    @Override
    public void updatePrice(SupplyRequestOptions shopOption) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getConnection();
            String sql = "UPDATE SupplyRequestOptions SET prices = ? where (itemName = ? AND shop = ?)";

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
