package com.tk_game.backend.controller;

import org.springframework.web.bind.annotation.*;
import com.tk_game.backend.service.AuthService;
import com.tk_game.backend.model.Account;
import com.tk_game.backend.dto.RegisterRequestDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
    Account acc = authService.register(request);
    return ResponseEntity.ok(Map.of("id", acc.getId()));
  }
}

