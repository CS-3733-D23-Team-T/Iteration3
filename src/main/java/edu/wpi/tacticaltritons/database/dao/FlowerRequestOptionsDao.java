package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.FlowerRequestOptions;
import edu.wpi.tacticaltritons.database.RequestOptions;

import java.sql.SQLException;
import java.util.List;

public interface FlowerRequestOptionsDao extends IDao<FlowerRequestOptions>{
    FlowerRequestOptions get(String itemName, String shop) throws SQLException;

    List<FlowerRequestOptions> getFromShop(String shop) throws SQLException;

    void updatePrice(FlowerRequestOptions flowerRequestOptions) throws SQLException;
}