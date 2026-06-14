package com.tk_game.backend.service;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.EnemyTemplate;
import com.tk_game.backend.repository.EnemyTemplateRepository;
import com.tk_game.backend.enume.StatType;
import com.tk_game.backend.gamelayer.StatsHelper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BattleService {

  private final EnemyTemplateRepository enemyRepo;
  private final StatsHelper statsHelper;

  public BattleService(EnemyTemplateRepository enemyRepo, StatsHelper statsHelper) {
      this.enemyRepo = enemyRepo;
      this.statsHelper = statsHelper;
  }

  public Map<String, Object> runBattle(Character character, Long enemyTemplateId) {
    EnemyTemplate enemy = enemyRepo.findById(enemyTemplateId).orElseThrow(() -> new RuntimeException("Enemy not found"));

    int lvl = character.getLevel();
    
    Map<StatType, Integer> Stats = statsHelper.calculateTotalStats(character);
    Integer charSTR = Stats.getOrDefault(StatType.STR, 1);
    Integer charDEX = Stats.getOrDefault(StatType.DEX, 1);
    Integer charINT = Stats.getOrDefault(StatType.INT, 1);
    Integer charCON = Stats.getOrDefault(StatType.CON, 1);
    Integer charLCK = Stats.getOrDefault(StatType.LCK, 1);

    int playerHp = lvl * charCON * 8 + 10 * charCON;

    int enemyHp = lvl * enemy.getBaseCon() + lvl * enemy.getBaseHp() + enemy.getBaseHp() * enemy.getBaseCon();

    double playerCrit = (double) charLCK / (lvl * lvl * 20.0);
    double playerDodge = (double) charDEX * lvl / 100.0;
    double enemyCrit = (double) enemy.getBaseLck() / (lvl * lvl * 20.0);
    double enemyDodge = (double) enemy.getBaseDex() * lvl / 100.0;

    Random rng = new Random();
    List<String> log = new ArrayList<>();
    int round = 1;

    while (playerHp > 0 && enemyHp > 0) {
      log.add("-- Runda " + round + " --");

      if (rng.nextDouble() < enemyDodge) {
        log.add("Przeciwnik uniknął ataku!");
      } else {
        int dmg = charSTR * (enemy.getBaseDmgMin() + rng.nextInt(Math.max(1, enemy.getBaseDmgMax() - enemy.getBaseDmgMin() + 1)));
        boolean crit = rng.nextDouble() < playerCrit;
        if (crit) dmg *= 2;
          enemyHp -= dmg;
          log.add("Gracz zadaje " + dmg + (crit ? " (CRIT!)" : "") + " dmg. HP wroga: " + Math.max(0, enemyHp));
      }

      if (enemyHp <= 0) break;

        if (rng.nextDouble() < playerDodge) {
          log.add("Gracz uniknął ataku!");
        } else {
            int dmg = enemy.getBaseStr() * (enemy.getBaseDmgMin() + rng.nextInt(Math.max(1, enemy.getBaseDmgMax() - enemy.getBaseDmgMin() + 1)));
            boolean crit = rng.nextDouble() < enemyCrit;
            if (crit) dmg *= 2;
              playerHp -= dmg;
            log.add("Wróg zadaje " + dmg + (crit ? " (CRIT!)" : "") + " dmg. HP gracza: " + Math.max(0, playerHp));
        }

        round++;
        if (round > 50) {
          log.add("Walka trwała zbyt długo – remis!");
          break;
        }
    }

    boolean playerWon = enemyHp <= 0;
    log.add(playerWon ? "Gracz wygrywa!" : "Gracz przegrywa!");

    Map<String, Object> result = new HashMap<>();
    result.put("won", playerWon);
    result.put("log", log);
    result.put("enemyName", enemy.getName());
    result.put("maxPlayerHp", lvl * charCON * 8 + 10 * charCON); 
    result.put("maxEnemyHp", lvl * enemy.getBaseCon() + lvl * enemy.getBaseHp() + enemy.getBaseHp() * enemy.getBaseCon());
    result.put("playerName", character.getName());
    return result;
  }
}
