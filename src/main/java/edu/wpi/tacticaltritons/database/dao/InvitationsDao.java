package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Invitations;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface InvitationsDao extends IDao<Invitations> {
    List<Invitations> getAll(String sessionFirstName, String sessionLastName, Date currentDate) throws SQLException;

    void update(Invitations invitation) throws SQLException;
}
