package com.tk_game.backend.service;

import com.tk_game.backend.model.Account;
import com.tk_game.backend.repository.AccountRepository;
import com.tk_game.backend.dto.RegisterRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final AccountRepository repo;
  private final CharacterService characterService;

  public AuthService(AccountRepository repo, CharacterService characterService) {
    this.repo = repo;
    this.characterService = characterService;
  }

  public Account register(RegisterRequestDTO dto) {
    Account acc = new Account();
    acc.setLogin(dto.getLogin());
    acc.setPasswordHash(dto.getPassword()); // plain do zmiany

    Account saved = repo.save(acc);

    characterService.createCharacter(saved);

    return saved;
  }
}
