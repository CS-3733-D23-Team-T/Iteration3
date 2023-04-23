package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public interface MoveDao extends IDao<Move> {
  Move get(Node node, LocationName location, Date moveDate) throws SQLException;

  List<Move> getAllCurrent(Date date) throws SQLException;

  List<Move> getAllFuture(Date currentDate) throws SQLException;
}
