package com.tk_game.backend.service;

import com.tk_game.backend.model.Account;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

  private final CharacterRepository repo;

  public CharacterService(CharacterRepository repo) {
    this.repo = repo;
  }

  public Character createCharacter(Account account) {
    Character c = new Character();
    c.setAccount(account);
    c.setName("Hero_" + account.getLogin());
    return repo.save(c);
  }

  public Character getByAccountId(Long accountId) {
    return repo.findByAccountId(accountId);
  }
}
