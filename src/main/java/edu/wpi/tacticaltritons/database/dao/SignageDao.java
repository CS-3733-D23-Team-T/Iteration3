package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Signage;

import java.sql.SQLException;

public interface SignageDao extends IDao<Signage>{

    void update(Signage sign) throws SQLException;

    Signage get(String title) throws SQLException;
}
