package com.tk_game.backend.service;
import com.tk_game.backend.model.Account;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.dto.CharacterDTO;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.AccountRepository;
import com.tk_game.backend.repository.ItemInstanceRepository;
import com.tk_game.backend.repository.ItemStatRepository;
import com.tk_game.backend.gamelayer.StatsHelper;
import com.tk_game.backend.enume.StatType;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class CharacterService {
  private final CharacterRepository characterRepository;
  private final AccountRepository accountRepository;
  private final ItemInstanceRepository itemInstanceRepository;
  private final ItemStatRepository itemStatRepository;
  private final StatsHelper statsHelper;

  public CharacterService(CharacterRepository characterRepository,
                          AccountRepository accountRepository,
                          ItemInstanceRepository itemInstanceRepository,
                          ItemStatRepository itemStatRepository,
                          StatsHelper statsHelper) {
    this.characterRepository = characterRepository;
    this.accountRepository = accountRepository;
    this.itemInstanceRepository = itemInstanceRepository;
    this.itemStatRepository = itemStatRepository;
    this.statsHelper = statsHelper;
  }

  public Character createCharacter(Long accountId, String name) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new RuntimeException("Account not found"));
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
    character.setSeed(new Random().nextLong());
    character.setQuestsCompleted(0);
    return characterRepository.save(character);
  }

  public Character getByAccountId(Long accountId) {
    return characterRepository.findByAccount_Id(accountId)
      .orElseThrow(() -> new RuntimeException("Character not found"));
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
    Map<StatType, Integer> calculatedStats = statsHelper.calculateTotalStats(c);
    Map<String, Integer> totalStatsMap = new HashMap<>();
    for (Map.Entry<StatType, Integer> entry : calculatedStats.entrySet()) {
        totalStatsMap.put(entry.getKey().name(), entry.getValue());
    }
    dto.totalStats = totalStatsMap;
    return dto;
  }

  public CharacterDTO upgradeStat(Long accountId, String statName) {
    Character character = characterRepository.findByAccount_Id(accountId)
        .orElseThrow(() -> new RuntimeException("Character not found"));
    String cleanStat = statName.toUpperCase();
    StatsHelper.upgradeStat(character, cleanStat);
    characterRepository.save(character);
    return toDTO(character);
  }

  public Character UpdateCharacter(Character c) {
    return characterRepository.save(c);
  }
}
