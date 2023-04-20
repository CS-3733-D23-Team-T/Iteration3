package edu.wpi.tacticaltritons.database;

import java.sql.Array;
import java.sql.Date;
import java.time.LocalDateTime;

public class Login {
  private String username;
  private String password;
  private String salt;
  private String email;
  private String firstName;
  private String lastName;
  private boolean admin;
  private LocalDateTime lastLogin;
  private boolean narration;
  private String language;
  private boolean twoFactor;
  private String[] twoFactorMethods;
  private String twoFactorFrequency;
  private int tokenTime;
  private String algorithmPreference;
  private boolean darkMode;

  public Login(
      String username,
      String salt,
      String password,
      String email,
      String firstName,
      String lastName,
      boolean admin,
      LocalDateTime lastLogin,
      boolean narration,
      String language,
      boolean twoFactor,
      String[] twoFactorMethods,
      String twoFactorFrequency,
      int tokenTime,
      String algorithmPreference,
      boolean darkMode) {
    this.username = username;
    this.salt = salt;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.admin = admin;
    this.lastLogin = lastLogin;
    this.narration = narration;
    this.language = language;
    this.twoFactor = twoFactor;
    this.twoFactorMethods = twoFactorMethods;
    this.twoFactorFrequency = twoFactorFrequency;
    this.tokenTime = tokenTime;
    this.algorithmPreference = algorithmPreference;
    this.darkMode = darkMode;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {return lastName;}

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public boolean getNarration() {
    return narration;
  }

  public void setNarration(boolean narration) {
    this.narration = narration;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public boolean getTwoFactor() {
    return twoFactor;
  }

  public void setTwoFactor(boolean twoFactor) {
    this.twoFactor = twoFactor;
  }

  public String[] getTwoFactorMethods() {
    return twoFactorMethods;
  }

  public void setTwoFactorMethods(String[] twoFactorMethods) {
    this.twoFactorMethods = twoFactorMethods;
  }

  public String getTwoFactorFrequency() {
    return twoFactorFrequency;
  }

  public void setTwoFactorFrequency(String twoFactorFrequency) {
    this.twoFactorFrequency = twoFactorFrequency;
  }

  public String getAlgorithmPreference() {
    return algorithmPreference;
  }

  public void setAlgorithmPreference(String algorithmPreference) {
    this.algorithmPreference = algorithmPreference;
  }

  public int getTokenTime() {
    return tokenTime;
  }

  public void setTokenTime(int tokenTime) {
    this.tokenTime = tokenTime;
  }

  public void setDarkMode(boolean darkMode) {
    this.darkMode = darkMode;
  }

  public boolean getDarkMode() {
    return darkMode;
  }
}
