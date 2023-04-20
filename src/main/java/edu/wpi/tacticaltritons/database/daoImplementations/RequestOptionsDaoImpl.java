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
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM RequestOptions WHERE itemName = ? AND restaurant  = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, itemName);
            ps.setString(2, restaurant);

            rs = ps.executeQuery();
            if (rs.next()) {
                int price = rs.getInt("prices");
                requestOptions = new RequestOptions(itemName, price, restaurant);
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
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM RequestOptions;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String itemName = rs.getString("itemName");
                int prices = rs.getInt("prices");
                String restaurant = rs.getString("restaurant");

                RequestOptions option = new RequestOptions(itemName, prices, restaurant);

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
            connection = Tdb.getConnection();
            String sql = "INSERT INTO RequestOptions (itemName, prices, restaurant) VALUES (?, ?, ?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1, requestOptions.getItemName());
            ps.setDouble(2, requestOptions.getPrice());
            ps.setString(3, requestOptions.getRestaurant());

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
            connection = Tdb.getConnection();
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
            connection = Tdb.getConnection();

            String sql = "SELECT * FROM RequestOptions WHERE restaurant = ?;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, restaurant);

            rs = ps.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("itemName");
                int prices = rs.getInt("prices");

                RequestOptions option = new RequestOptions(itemName, prices, restaurant);

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
            connection = Tdb.getConnection();
            String sql = "UPDATE RequestOptions SET prices = ? where (itemName = ? AND restaurant = ?)";

            ps = connection.prepareStatement(sql);

            ps.setDouble(1, restaurantOption.getPrice());
            ps.setString(2, restaurantOption.getItemName());
            ps.setString(3, restaurantOption.getRestaurant());

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