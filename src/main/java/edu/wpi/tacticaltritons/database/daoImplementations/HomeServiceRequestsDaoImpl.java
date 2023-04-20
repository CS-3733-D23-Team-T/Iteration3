package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.HomeServiceRequests;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.HomeServiceRequestsDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HomeServiceRequestsDaoImpl implements HomeServiceRequestsDao {

    @Override
    public List<HomeServiceRequests> getAll() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<HomeServiceRequests> homeServiceRequests = new ArrayList<>();

        try {
            connection = Tdb.getConnection();
            String sql = "Select * FROM" +
                            "(SELECT L.firstname, L.lastname, 'Meal' as requestType, M.ordernum, M.deliverydate, M.deliverytime" +
                            "FROM Login L" +
                            "         JOIN meal M ON L.firstname = M.assignedstafffirst AND L.lastname = M.assignedstafflast" +
                            "WHERE M.status = 'Processing'" +
                            "UNION ALL" +
                            "SELECT L.firstname, L.lastname, 'Flower' as requestType, F.ordernum, F.deliverydate, F.deliverytime" +
                            "FROM Login L" +
                            "         JOIN flower F ON L.firstname = F.assignedstafffirst and L.lastname = F.assignedstafflast" +
                            "WHERE F.status = 'Processing') as P" +
                         "ORDER BY P.deliverydate, P.deliverytime;";
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);

            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String requestType = rs.getString("requestType");
                int orderNum = rs.getInt("ordeNum");
                Date deliveryDate = rs.getDate("deliveryDate");
                Time deliveryTime = rs.getTime("deliveryTime");

                HomeServiceRequests request = new HomeServiceRequests(firstName,lastName,requestType,orderNum,deliveryDate,deliveryTime);

                homeServiceRequests.add(request);
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
        return homeServiceRequests;
    }

    @Override
    public List<HomeServiceRequests> getAll(String sessionFirstName, String sessionLastName) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<HomeServiceRequests> homeServiceRequests = new ArrayList<>();

        try {
            connection = Tdb.getConnection();
            String sql = "Select * FROM" +
                            "(SELECT L.firstname, L.lastname, 'Meal' as requestType, M.ordernum, M.deliverydate, M.deliverytime " +
                            "FROM Login L " +
                            "         JOIN meal M ON L.firstname = M.assignedstafffirst AND L.lastname = M.assignedstafflast " +
                            "WHERE M.status = 'Processing' " +
                            "UNION ALL " +
                            "SELECT L.firstname, L.lastname, 'Flower' as requestType, F.ordernum, F.deliverydate, F.deliverytime " +
                            "FROM Login L " +
                            "         JOIN flower F ON L.firstname = F.assignedstafffirst and L.lastname = F.assignedstafflast " +
                            "WHERE F.status = 'Processing') as P " +
                         "WHERE firstName = ? AND lastName = ? " +
                         "ORDER BY P.deliverydate, P.deliverytime;";
            ps = connection.prepareStatement(sql);
            ps.setString(1, sessionFirstName);
            ps.setString(2, sessionLastName);

            rs = ps.executeQuery();

            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String requestType = rs.getString("requestType");
                int orderNum = rs.getInt("orderNum");
                Date deliveryDate = rs.getDate("deliveryDate");
                Time deliveryTime = rs.getTime("deliveryTime");

                HomeServiceRequests request = new HomeServiceRequests(firstName,lastName,requestType,orderNum,deliveryDate,deliveryTime);

                homeServiceRequests.add(request);
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
        return homeServiceRequests;
    }

    @Override
    public void insert(HomeServiceRequests homeServiceRequests) throws SQLException {

    }

    @Override
    public void delete(HomeServiceRequests homeServiceRequests) throws SQLException {

    }
}
