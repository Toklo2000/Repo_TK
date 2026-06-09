package com.tk_game.backend.service;

import com.tk_game.backend.model.Account;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.dto.CharacterDTO;

import org.springframework.stereotype.Service;

@Service
public class CharacterService {

  private final CharacterRepository characterRepository;

  public CharacterService(CharacterRepository characterRepository) {
    this.characterRepository = characterRepository;
  }

  public Character createCharacter(Account account, String name) {
    Character character = new Character();
    character.setAccount(account);
    character.setName(name);

    character.setLevel(1);
    character.setExp(0);
    character.setGold(100);
    character.setEnergy(100);
    
    character.setStrength(1);
    character.setDexterity(1);
    character.setIntelligence(1);
    character.setConstitution(1);
    character.setLuck(1);

    return characterRepository.save(character);
  }

  public Character getByAccountId(Long accountId) {
    return characterRepository.findByAccount_Id(accountId);
  }

  public CharacterDTO toDTO(Character c) {
    CharacterDTO dto = new CharacterDTO();

    dto.id = c.getId();
    dto.name = c.getName();

    dto.level = c.getLevel();
    dto.exp = c.getExp();
    dto.gold = c.getGold();

    dto.strength = c.getStrength();
    dto.dexterity = c.getDexterity();
    dto.intelligence = c.getIntelligence();
    dto.constitution = c.getConstitution();
    dto.luck = c.getLuck();

    return dto;
  }
}
