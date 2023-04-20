package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Furniture;

import java.sql.SQLException;

public interface FurnitureDao extends IDao<Furniture>{
    Furniture get(int orderNum) throws SQLException;

    void update(Furniture furniture) throws SQLException;
}
