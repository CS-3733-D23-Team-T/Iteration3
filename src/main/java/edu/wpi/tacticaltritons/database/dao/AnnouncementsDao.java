package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Announcements;

import java.sql.SQLException;
import java.util.UUID;

public interface AnnouncementsDao extends IDao<Announcements> {
    void update(Announcements announcements) throws SQLException;

    Announcements get(UUID id) throws SQLException;
}
