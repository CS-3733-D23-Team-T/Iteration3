package edu.wpi.tacticaltritons.database;

import java.sql.*;

public enum Tdb {

  INSTANCE;
  private static final String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamtdb";
  private static final String user = "teamt";
  private static final String pass = "teamt170";


  private static Connection connection;


  public static Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName("org.postgresql.Driver");
    if(connection == null || connection.isClosed()){
      connection = createConnection();
    }
    return connection;
  }

  private static Connection createConnection() {
    try {
      Class.forName("org.postgresql.Driver");
      connection = DriverManager.getConnection(url, user, pass);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return connection;
  }
}