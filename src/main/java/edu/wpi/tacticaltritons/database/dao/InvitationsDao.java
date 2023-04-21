package edu.wpi.tacticaltritons.database.dao;

import edu.wpi.tacticaltritons.database.Invitations;

import java.sql.SQLException;
import java.util.List;

public interface InvitationsDao extends IDao<Invitations> {
    List<Invitations> getAll(String firstName, String lastName) throws SQLException;
}
