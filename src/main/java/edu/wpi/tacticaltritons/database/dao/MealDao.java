package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Meal;
import java.sql.SQLException;

public interface MealDao extends IDao<Meal> {
  void update(Meal meal) throws SQLException;

  Meal get(int orderNum) throws SQLException;
}
