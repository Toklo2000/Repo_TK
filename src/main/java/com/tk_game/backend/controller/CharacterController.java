package com.tk_game.backend.controller;

import com.tk_game.backend.model.Account;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
public class CharacterController {

  private final CharacterRepository characterRepository;
  private final AccountRepository accountRepository;

  public CharacterController(CharacterRepository characterRepository, AccountRepository accountRepository) {
    this.characterRepository = characterRepository;
    this.accountRepository = accountRepository;
  }

  @PostMapping("/create")
  public Character create(@RequestParam Long accountId, @RequestParam String name) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new RuntimeException("Account not found"));

    Character character = new Character();
    character.setAccount(account);
    character.setName(name);

    character.setLevel(1);
    character.setExp(0);
    character.setGold(100);
    character.setEnergy(100);
    
    return characterRepository.save(character);
  }

  @GetMapping("/{accountId}")
  public Character get(@PathVariable Long accountId) {
    return characterRepository.findByAccount_Id(accountId);
  }

}
