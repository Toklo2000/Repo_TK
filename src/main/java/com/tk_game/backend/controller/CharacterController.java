package com.tk_game.backend.controller;

import com.tk_game.backend.service.CharacterService;
import com.tk_game.backend.dto.CharacterDTO;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.Account;
import com.tk_game.backend.repository.ItemInstanceRepository;
import com.tk_game.backend.repository.ItemStatRepository;
import com.tk_game.backend.repository.AccountRepository;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.gamelayer.StatsHelper;
import com.tk_game.backend.enume.StatType;

import org.springframework.http.ResponseEntity; 
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/character")
public class CharacterController {
  private final CharacterService characterService;
  private final ItemInstanceRepository itemInstanceRepository;
  private final ItemStatRepository itemStatRepository;
  private final AccountRepository accountRepository;
  private final CharacterRepository characterRepository;
  public CharacterController(CharacterService characterService, ItemInstanceRepository itemInstanceRepository, ItemStatRepository itemStatRepository, AccountRepository accountRepository, CharacterRepository characterRepository) {
    this.characterService = characterService;
    this.itemInstanceRepository = itemInstanceRepository;
    this.itemStatRepository = itemStatRepository;
    this.accountRepository = accountRepository;
    this.characterRepository = characterRepository;
  }
  @PostMapping("/create")
  public Character create(@RequestParam Long accountId, @RequestParam String name) {
    return characterService.createCharacter(accountId, name);
  }
  @GetMapping("/{accountId}")
  public CharacterDTO get(@PathVariable Long accountId) {
    Character character = characterService.getByAccountId(accountId);
    return characterService.toDTO(character);
  }
  @PostMapping("/{accountId}/upgrade")
  public CharacterDTO upgradeStat(@PathVariable Long accountId, @RequestParam String stat) {
    return characterService.upgradeStat(accountId, stat);
  }
  @GetMapping("/{accountId}/stats")
  public ResponseEntity<Map<String, Integer>> getCharacterStats(@PathVariable Long accountId) {
    Character character = characterService.getByAccountId(accountId);
    Map<String, Integer> stats = new HashMap<>();
    
    stats.put("STR", character.getStrength());
    stats.put("DEX", character.getDexterity());
    stats.put("INT", character.getIntelligence());
    stats.put("CON", character.getConstitution());
    stats.put("LCK", character.getLuck());
    
    return ResponseEntity.ok(stats);
  }

  @GetMapping("/exists")
  public boolean exists(@AuthenticationPrincipal UserDetails userDetails) {
    Account account = accountRepository.findByLogin(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("Account not found"));

    return characterRepository.findByAccount_Id(account.getId()).isPresent();
  }
}
