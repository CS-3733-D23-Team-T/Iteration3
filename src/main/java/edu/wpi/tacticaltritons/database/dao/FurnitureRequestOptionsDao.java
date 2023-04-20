package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.FurnitureRequestOptions;

import java.sql.SQLException;
import java.util.List;

public interface FurnitureRequestOptionsDao extends IDao<FurnitureRequestOptions>{
    FurnitureRequestOptions get(String itemName) throws SQLException;


}
