package edu.wpi.tacticaltritons.database;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.Locale;

public enum Tdb {
  AWS_DATABASE("jdbc:postgresql://softeng.c280prrsyadq.us-east-2.rds.amazonaws.com:5432/postgres", "teamt", "CS3733yay"),
  WPI_DATABASE("jdbc:postgresql://database.cs.wpi.edu:5432/teamtdb", "teamt", "teamt170");

  private final String url;
  private final String user;
  private final String pass;
  private static Tdb INSTANCE = WPI_DATABASE;
  Tdb(String url, String user, String pass){
    this.url = url;
    this.user = user;
    this.pass = pass;
  }
  public String formalName(){
    String[] strings = this.name().split("_");
    StringBuilder sb = new StringBuilder();
    for(String string : strings){
      if(string.equals("DATABASE")){
        sb.append(string.substring(0,1)).append(string.substring(1).toLowerCase());

      }
      else {
        sb.append(string).append(" ");
      }
    }
    return sb.substring(0,sb.length());
  }
  public static Tdb parseTdb(String string){
    if(string == null) return null;
    string = string.toUpperCase(Locale.ROOT);
    string = string.replaceAll(" ", "_");

    if(string.equals(AWS_DATABASE.name())) return AWS_DATABASE;
    else if(string.equals(WPI_DATABASE.name())) return WPI_DATABASE;

    return null;
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