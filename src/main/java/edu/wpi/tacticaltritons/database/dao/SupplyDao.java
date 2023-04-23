package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Supply;

import java.sql.SQLException;

public interface SupplyDao extends IDao<Supply> {
  Supply get(int orderNum) throws SQLException;

  void update(Supply supply) throws SQLException;
}
