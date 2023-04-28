package edu.wpi.tacticaltritons.database.daoImplementations;

import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.database.dao.LoginDao;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginDaoImpl implements LoginDao {

  @Override
  public Login get(String username) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    Login login = null;
    try {
      connection = Tdb.getInstance().getConnection();


      String sql = "SELECT * FROM Login WHERE username = ?;";
      ps = connection.prepareStatement(sql);
      ps.setString(1, username);

      rs = ps.executeQuery();
      if (rs.next()) {
        login = mapResult(rs, username);
      }
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(rs != null){
        rs.close();
      }
      if(ps != null){
        ps.close();
      }
    }
    return login;
  }

  @Override
  public List<Login> getAll() throws SQLException {
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    List<Login> logins = new ArrayList<>();
    try {
      connection = Tdb.getInstance().getConnection();

      String sql = "SELECT * FROM Login;";
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        String username = rs.getString("username");
        Login login = mapResult(rs, username);

        logins.add(login);
      }
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(rs != null){
        rs.close();
      }
      if(statement != null){
        statement.close();
      }
    }
    return logins;
  }

  private Login mapResult(ResultSet rs, String username) throws SQLException {
    String salt = rs.getString("salt");
    String password = rs.getString("password");
    String email = rs.getString("email");
    String firstName = rs.getString("firstName");
    String lastName = rs.getString("lastName");
    boolean admin = rs.getBoolean("admin");
    Timestamp lastLogin = rs.getTimestamp("lastLogin");
    boolean narration = rs.getBoolean("narration");
    String language = rs.getString("language");
    boolean twoFactor = rs.getBoolean("twoFactor");
    Array raw2FAMethods = rs.getArray("twoFactorMethods");
    String[] twoFactorMethods = null;
    if(raw2FAMethods != null){
      twoFactorMethods = (String[]) raw2FAMethods.getArray();
    }
    String twoFactorFrequency = rs.getString("twoFactorFrequency");
    int tokenTime = rs.getInt("tokenTime");
    String algorithmPreference = rs.getString("algorithmPreference");
    boolean darkMode = rs.getBoolean("darkMode");
    String database = rs.getString("database");

    LocalDateTime lastLog = lastLogin == null ? null : LocalDateTime.ofInstant(lastLogin.toInstant(), ZoneId.systemDefault());
    return new Login(username,
            salt,
            password,
            email,
            firstName,
            lastName,
            admin,
            lastLog,
            narration,
            language,
            twoFactor,
            twoFactorMethods,
            twoFactorFrequency,
            tokenTime,
            algorithmPreference,
            darkMode,
            database);
  }

  @Override
  public void insert(Login login) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql =
              "INSERT INTO Login (username, " +
                      "email, " +
                      "firstName, " +
                      "lastName, " +
                      "admin, " +
                      "password, " +
                      "salt, " +
                      "lastLogin, " +
                      "narration, " +
                      "language, " +
                      "twoFactor, " +
                      "twoFactorMethods, " +
                      "twoFactorFrequency, " +
                      "tokenTime, " +
                      "algorithmPreference, " +
                      "darkMode, " +
                      "database)" +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

      ps = connection.prepareStatement(sql);
      mapStatement(ps, login, connection,true);

      int result = ps.executeUpdate();
    } catch (SQLException | ClassNotFoundException e){
      e.printStackTrace();
    } finally {
      if(rs != null){
        rs.close();
      }
      if(ps != null){
        ps.close();
      }
    }
  }

  @Override
  public void update(Login login) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql =
              "UPDATE Login SET " +
                      "email = ?, " +
                      "firstName = ?, " +
                      "lastName = ?, " +
                      "admin = ?, " +
                      "password = ?, " +
                      "salt = ?, " +
                      "lastLogin = ?, " +
                      "narration = ?, " +
                      "language = ?, " +
                      "twoFactor = ?, " +
                      "twoFactorMethods = ?, " +
                      "twoFactorFrequency = ?, " +
                      "tokenTime = ?, " +
                      "algorithmPreference = ?, " +
                      "darkMode = ?, " +
                      "database = ? " +
                      "WHERE username = ?";

      ps = connection.prepareStatement(sql);
      mapStatement(ps, login, connection,false);

      int result = ps.executeUpdate();
    } catch (SQLException | ClassNotFoundException e){
      e.printStackTrace();
    } finally {
      if(rs != null){
        rs.close();
      }
      if(ps != null){
        ps.close();
      }
    }
  }

  private void mapStatement(PreparedStatement statement, Login login, Connection connection, boolean type) throws SQLException {
    Timestamp timestamp = login.getLastLogin() == null ? null : Timestamp.valueOf(login.getLastLogin());

    statement.setString(type ? 1 : 16, login.getUsername());
    statement.setString(type ? 2 : 1, login.getEmail());
    statement.setString(type ? 3 : 2, login.getFirstName());
    statement.setString(type ? 4 : 3, login.getLastName());
    statement.setBoolean(type ? 5 : 4, login.isAdmin());
    statement.setString(type ? 6 : 5, login.getPassword());
    statement.setString(type ? 7 : 6, login.getSalt());
    statement.setTimestamp(type ? 8 : 7, timestamp);
    statement.setBoolean(type ? 9 : 8, login.getNarration());
    statement.setString(type ? 10 : 9, login.getLanguage());
    statement.setBoolean(type ? 11 : 10, login.getTwoFactor());
    statement.setArray(type ? 12 : 11, connection.createArrayOf("VARCHAR", login.getTwoFactorMethods()));
    statement.setString(type ? 13 : 12, login.getTwoFactorFrequency());
    statement.setInt(type ? 14 : 13, login.getTokenTime());
    statement.setString(type ? 15 : 14, login.getAlgorithmPreference());
    statement.setBoolean(type ? 16 : 15, login.getDarkMode());
    statement.setString(type ? 17 : 16, login.getDatabase());
  }

  @Override
  public void delete(Login login) throws SQLException {
    Connection connection = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      connection = Tdb.getInstance().getConnection();
      String sql = "DELETE FROM Login WHERE username = ?";

      ps = connection.prepareStatement(sql);

      ps.setString(1, login.getUsername());

      int result = ps.executeUpdate();
    } catch (SQLException e){
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } finally {
      if(rs != null){
        rs.close();
      }
      if(ps != null){
        ps.close();
      }
    }
  }
}
