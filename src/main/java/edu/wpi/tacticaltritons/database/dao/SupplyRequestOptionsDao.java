package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.SupplyRequestOptions;

import java.sql.SQLException;
import java.util.List;

public interface SupplyRequestOptionsDao extends IDao<SupplyRequestOptions>{
    SupplyRequestOptions get(String itemName, String shop) throws SQLException;

    List<SupplyRequestOptions> getFromShop(String shop) throws SQLException;

    void updatePrice(SupplyRequestOptions supplyRequestOptions) throws SQLException;
}