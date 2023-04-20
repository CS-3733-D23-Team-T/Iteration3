package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.RequestOptions;
import java.sql.SQLException;
import java.util.List;

public interface RequestOptionsDao extends IDao<RequestOptions> {
  RequestOptions get(String itemName, String restaurant) throws SQLException;

  List<RequestOptions> getFromRestaurant(String restaurant) throws SQLException;

  void updatePrice(RequestOptions restaurantOption) throws SQLException;
}
