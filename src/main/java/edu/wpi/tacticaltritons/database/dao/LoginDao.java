package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Login;
import java.sql.SQLException;

public interface LoginDao extends IDao<Login> {
  Login get(String username) throws SQLException;

  void update(Login login) throws SQLException;
}
