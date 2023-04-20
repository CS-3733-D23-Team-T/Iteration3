package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Conference;
import edu.wpi.tacticaltritons.database.Flower;
import edu.wpi.tacticaltritons.database.FlowerRequestOptions;

import java.sql.SQLException;

public interface ConferenceDao extends IDao<Conference>{
    Conference get(int orderNum) throws SQLException;

    void update(Conference conference) throws SQLException;
}
