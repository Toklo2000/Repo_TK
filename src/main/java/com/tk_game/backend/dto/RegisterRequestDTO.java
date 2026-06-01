package com.tk_game.backend.dto;

public class RegisterRequestDTO {

  private String login;
  private String email;
  private String password;
  private String date;

  public String getLogin() { return login; }
  public void setLogin(String login) { this.login = login; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getDate() { return date; }
  public void setDate(String date) { this.date = date; }
}
