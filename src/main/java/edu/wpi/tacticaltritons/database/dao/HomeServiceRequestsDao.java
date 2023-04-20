package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.FurnitureRequestOptions;
import edu.wpi.tacticaltritons.database.HomeServiceRequests;

import java.sql.SQLException;
import java.util.List;

public interface HomeServiceRequestsDao extends IDao<HomeServiceRequests>{
    List<HomeServiceRequests> getAll(String sessionFirstName, String sessionLastName) throws SQLException;
}
