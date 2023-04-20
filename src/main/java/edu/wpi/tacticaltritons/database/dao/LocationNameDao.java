package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.LocationName;
import java.sql.SQLException;

public interface LocationNameDao extends IDao<LocationName> {
  LocationName get(String locationName) throws SQLException;

  void update(LocationName locationName) throws SQLException;

    void update(LocationName oldLocationName, LocationName newLocationName) throws SQLException;
}
