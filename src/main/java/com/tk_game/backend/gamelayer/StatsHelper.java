package com.tk_game.backend.gamelayer;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.*;
import com.tk_game.backend.enume.EquipmentSlot;
import com.tk_game.backend.enume.StatType;
import com.tk_game.backend.repository.ItemInstanceRepository;
import com.tk_game.backend.repository.ItemStatRepository;

import java.lang.Math;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StatsHelper {
  public static int calculateUpgradeCost(Character c, String stat) {
    int value = getStat(c, stat);
    return (int)Math.floor((double)value / 20 + 1) * 5 * value; //x^2 / 4 ~
  }

  public static void upgradeStat(Character c, String stat) {
    int cost = calculateUpgradeCost(c, stat);

    if (c.getGold() < cost) {
      throw new RuntimeException("Not enough gold");
    }

    c.setGold(c.getGold() - cost);

    switch (stat) {
      case "strength" -> c.setStrength(c.getStrength() + 1);
      case "dexterity" -> c.setDexterity(c.getDexterity() + 1);
      case "intelligence" -> c.setIntelligence(c.getIntelligence() + 1);
      case "constitution" -> c.setConstitution(c.getConstitution() + 1);
      case "luck" -> c.setLuck(c.getLuck() + 1);
      default -> throw new RuntimeException("Invalid stat");
    }
  }

  private static int getStat(Character c, String stat) {
    return switch (stat) {
      case "strength" -> c.getStrength();
      case "dexterity" -> c.getDexterity();
      case "intelligence" -> c.getIntelligence();
      case "constitution" -> c.getConstitution();
      case "luck" -> c.getLuck();
      default -> throw new RuntimeException("Invalid stat");
    };
  }

  public static Map<StatType, Integer> calculateTotalStats(
      Character character,
      ItemInstanceRepository itemRepo,
      ItemStatRepository statRepo ) {

    Map<StatType, Integer> result = new EnumMap<>(StatType.class);

    result.put(StatType.STR, character.getStrength());
    result.put(StatType.DEX, character.getDexterity());
    result.put(StatType.INT, character.getIntelligence());
    result.put(StatType.CON, character.getConstitution());
    result.put(StatType.LCK, character.getLuck());

    List<ItemInstance> items = itemRepo.findByOwner(character);

    for (ItemInstance item : items) {

      if (item.getSlot() == EquipmentSlot.UNEQUIPPED)
        continue;

      List<ItemStat> stats = statRepo.findByItemInstance(item);

      for (ItemStat s : stats) {
        result.put(
          s.getType(),
          result.getOrDefault(s.getType(), 0) + s.getValue()
        );
      }
    }
    return result;
  }
}
