package edu.wpi.tacticaltritons.database.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDao<T> {
  List<T> getAll() throws SQLException;

  void insert(T t) throws SQLException;

  void delete(T t) throws SQLException;
}
