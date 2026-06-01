package com.tk_game.backend.controller;

import com.tk_game.backend.model.Account;
import com.tk_game.backend.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

  private final AccountRepository repo;

  public TestController(AccountRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public String test() {
    return "OK RPG BACKEND WORKING";
  }

  @GetMapping("/accounts")
  public List<Account> getAllAccounts() {
    return repo.findAll();
  }
}
