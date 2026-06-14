package com.tk_game.backend.controller;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.EnemyTemplate;
import com.tk_game.backend.model.Mission;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.EnemyTemplateRepository;
import com.tk_game.backend.repository.MissionRepository;
import com.tk_game.backend.service.BattleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/battle")
public class BattleController {

  private final BattleService battleService;
  private final CharacterRepository characterRepo;
  private final EnemyTemplateRepository enemyRepo;
  private final MissionRepository missionRepo;

  public BattleController(BattleService battleService, CharacterRepository characterRepo, EnemyTemplateRepository enemyRepo, MissionRepository missionRepo) {
    this.battleService = battleService;
    this.characterRepo = characterRepo;
    this.enemyRepo = enemyRepo;
    this.missionRepo = missionRepo;
  }

  @PostMapping("/run")
  public ResponseEntity<Map<String, Object>> runBattle(@RequestParam Long characterId, @RequestParam Long enemyId) {
    Character character = characterRepo.findById(characterId).orElseThrow(() -> new RuntimeException("Character not found"));
    Map<String, Object> result = battleService.runBattle(character, enemyId);
    return ResponseEntity.ok(result);
  }

  @PostMapping("/run-for-quest")
  public ResponseEntity<Map<String, Object>> runBattleForQuest(@RequestParam Long characterId) {

    Character character = characterRepo.findById(characterId).orElseThrow(() -> new RuntimeException("Character not found"));

      Optional<Mission> activeMission = missionRepo.findActiveByCharacter(character.getId());

      EnemyTemplate enemy;

      if (activeMission.isPresent() && activeMission.get().getEnemyTemplateId() != null) {
        enemy = enemyRepo.findById(activeMission.get().getEnemyTemplateId()).orElseThrow(() -> new RuntimeException("Enemy not found"));
      } else {
        List<EnemyTemplate> enemies = enemyRepo.findAll();
        if (enemies.isEmpty()) {
          return ResponseEntity.badRequest().body(Map.of("error", "No enemies found"));
        }
        Random rng = new Random(character.getId() + System.currentTimeMillis() / 10000);
        enemy = enemies.get(rng.nextInt(enemies.size()));

        if (activeMission.isPresent()) {
          Mission mission = activeMission.get();
          mission.setEnemyTemplateId(enemy.getId());
          missionRepo.save(mission);
        }
      }

      Map<String, Object> result = battleService.runBattle(character, enemy.getId());
      return ResponseEntity.ok(result);
  }
}
