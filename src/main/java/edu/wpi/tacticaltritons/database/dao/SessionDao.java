package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Session;

import java.sql.SQLException;
import java.util.UUID;

public interface SessionDao extends IDao<Session> {

    Session get(String username) throws SQLException;
    void update(Session session) throws SQLException;
}
