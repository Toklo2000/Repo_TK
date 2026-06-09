package com.tk_game.backend.controller;

import com.tk_game.backend.service.CharacterService;
import com.tk_game.backend.dto.CharacterDTO;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.model.Account;
import com.tk_game.backend.repository.AccountRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/character")
public class CharacterController {

  private final AccountRepository accountRepository;
  private final CharacterRepository characterRepository;
  private final CharacterService characterService;

  public CharacterController(CharacterService characterService, AccountRepository accountRepository, CharacterRepository characterRepository) {
    this.characterService = characterService;
    this.accountRepository = accountRepository;
    this.characterRepository = characterRepository;
  }

  @PostMapping("/create")
  public Character create(@RequestParam Long accountId, @RequestParam String name) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new RuntimeException("Account not found"));

    return characterService.createCharacter(account, name);
  }

  @GetMapping("/{accountId}")
  public CharacterDTO get(@PathVariable Long accountId) {
    Character character = characterService.getByAccountId(accountId);
    return characterService.toDTO(character);
  }

  @PostMapping("/{accountId}/upgrade")
  public CharacterDTO upgradeStat(@PathVariable Long accountId, @RequestParam String stat) {
    Character character = characterService.getByAccountId(accountId);
    int cost = 5;
    if (character.getGold() < cost) {
      throw new RuntimeException("Not enough gold");
    }

    character.setGold(character.getGold() - cost);

    switch (stat) {
      case "strength" -> character.setStrength(character.getStrength() + 1);
      case "dexterity" -> character.setDexterity(character.getDexterity() + 1);
      case "intelligence" -> character.setIntelligence(character.getIntelligence() + 1);
      case "constitution" -> character.setConstitution(character.getConstitution() + 1);
      case "luck" -> character.setLuck(character.getLuck() + 1);
    }
    
    Character saved = characterRepository.save(character);
    return characterService.toDTO(character);
  }
}
