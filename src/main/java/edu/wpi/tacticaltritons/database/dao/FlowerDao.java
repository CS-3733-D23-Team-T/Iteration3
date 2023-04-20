package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Flower;
import java.sql.SQLException;

public interface FlowerDao extends IDao<Flower> {
  Flower get(int orderNum) throws SQLException;

  void update(Flower flower) throws SQLException;
}
