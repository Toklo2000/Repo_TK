package com.tk_game.backend.dto;

import jakarta.validation.constraints.*;

public class RegisterRequestDTO {

  @NotBlank(message = "Login is required")
  @Size(min = 3, max = 20, message = "Login must be 3-20 characters")
  private String login;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

  @NotBlank(message = "Date is required")
  @Pattern(
    regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$",
    message = "Date must be in format dd/mm/yyyy"
  )
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
