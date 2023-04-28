package edu.wpi.tacticaltritons.database;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;

public enum Tdb {
  AWS("jdbc:postgresql://softeng.c280prrsyadq.us-east-2.rds.amazonaws.com:5432/postgres", "teamt", "CS3733yay"),
  WPI_DB("jdbc:postgresql://database.cs.wpi.edu:5432/teamtdb", "teamt", "teamt170");

  private final String url;
  private final String user;
  private final String pass;
  private static Tdb INSTANCE = WPI_DB;
  Tdb(String url, String user, String pass){
    this.url = url;
    this.user = user;
    this.pass = pass;
  }
  
  public static Tdb getInstance(){
    return INSTANCE;
  }
  public static void setInstance(Tdb instance){
    INSTANCE = instance;
  }

  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName("org.postgresql.Driver");
    return DriverManager.getConnection(this.url, this.user, this.pass);
  }

}