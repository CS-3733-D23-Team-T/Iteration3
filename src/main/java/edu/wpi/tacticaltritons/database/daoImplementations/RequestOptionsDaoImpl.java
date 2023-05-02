package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.dao.RequestOptionsDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestOptionsDaoImpl implements RequestOptionsDao {
    @Override
    public RequestOptions get(String itemName, String restaurant) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        RequestOptions requestOptions = null;
        try {
            connection = Tdb.getInstance().getConnection();

            String sql = "SELECT * FROM RequestOptions WHERE itemName = ? AND restaurant  = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, itemName);
            ps.setString(2, restaurant);

            rs = ps.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemDescription = rs.getString("itemDescription");
                String itemType = rs.getString("itemType");
                requestOptions = new RequestOptions(itemName, price, restaurant, shopDescription, itemDescription, itemType);
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
        return requestOptions;
    }
    @Override
    public List<RequestOptions> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<RequestOptions> requestOptions = new ArrayList<>();
        try {
            connection = Tdb.getInstance().getConnection();

            String sql = "SELECT * FROM RequestOptions;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String itemName = rs.getString("itemName");
                String restaurant = rs.getString("restaurant");
                double price = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemDescription = rs.getString("itemDescription");
                String itemType = rs.getString("itemType");

                RequestOptions option = new RequestOptions(itemName, price, restaurant, shopDescription, itemDescription, itemType);

                requestOptions.add(option);
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
        }
        return requestOptions;
    }

    @Override
    public void insert(RequestOptions requestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getInstance().getConnection();
            String sql = "INSERT INTO RequestOptions (itemName, prices, restaurant, shopDescription, itemDescription, itemType) VALUES (?, ?, ?, ?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, requestOptions.getItemName());
            ps.setDouble(2, requestOptions.getPrice());
            ps.setString(3, requestOptions.getRestaurant());
            ps.setString(4, requestOptions.getShopDescription());
            ps.setString(5, requestOptions.getItemDescription());
            ps.setString(6, requestOptions.getItemType());

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
    public void delete(RequestOptions requestOptions) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getInstance().getConnection();
            String sql = "DELETE FROM RequestOptions WHERE (itemName = ? AND restaurant = ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, requestOptions.getItemName());
            ps.setString(2, requestOptions.getRestaurant());

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
    public List<RequestOptions> getFromRestaurant(String restaurant) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RequestOptions> requestOptions = new ArrayList<>();
        try {
            connection = Tdb.getInstance().getConnection();

            String sql = "SELECT * FROM RequestOptions WHERE restaurant = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, restaurant);

            rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("itemName");
                double prices = rs.getDouble("prices");
                String shopDescription = rs.getString("shopDescription");
                String itemDescription = rs.getString("itemDescription");
                String itemType = rs.getString("itemType");

                RequestOptions option = new RequestOptions(itemName, prices, restaurant, shopDescription, itemDescription, itemType);

                requestOptions.add(option);
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
        }
        return requestOptions;
    }

    @Override
    public void updatePrice(RequestOptions restaurantOption) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Tdb.getInstance().getConnection();
            String sql = "UPDATE RequestOptions SET prices = ?, shopDescription = ?, itemDescription = ?, itemType = ? where (itemName = ? AND restaurant = ?)";

            ps = connection.prepareStatement(sql);

            ps.setDouble(1, restaurantOption.getPrice());
            ps.setString(2, restaurantOption.getShopDescription());
            ps.setString(3, restaurantOption.getItemDescription());
            ps.setString(4, restaurantOption.getItemType());
            ps.setString(5, restaurantOption.getItemName());
            ps.setString(6, restaurantOption.getRestaurant());

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