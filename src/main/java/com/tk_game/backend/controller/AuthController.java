package com.tk_game.backend.controller;

import org.springframework.web.bind.annotation.*;
import com.tk_game.backend.service.AuthService;
import com.tk_game.backend.model.Account;
import com.tk_game.backend.dto.RegisterRequestDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public Account register(@RequestBody RegisterRequestDTO dto) {
    if (!dto.getDate().matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$")) {
      throw new RuntimeException("Date must be dd/mm/yyyy");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate userDate = LocalDate.parse(dto.getDate(), formatter);
    LocalDate serverDate = LocalDate.now();

    if (!userDate.equals(serverDate)) {
      throw new RuntimeException("Date must be today's date: " + serverDate);
    }

    return authService.register(dto);
  }
}
