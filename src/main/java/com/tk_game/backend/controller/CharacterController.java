package com.tk_game.backend.controller;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.CharacterRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
public class CharacterController {

  private final CharacterRepository repo;

  public CharacterController(CharacterRepository repo) {
    this.repo = repo;
  }

  @GetMapping("/{accountId}")
  public Character get(@PathVariable Long accountId) {
    return repo.findByAccountId(accountId);
  }
}
