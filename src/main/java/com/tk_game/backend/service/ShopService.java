package com.tk_game.backend.service;

import com.tk_game.backend.gamelayer.ItemLogic;
import com.tk_game.backend.model.ItemTemplate;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.ItemTemplateRepository;
import com.tk_game.backend.enume.StatType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ShopService {

  private final ItemTemplateRepository itemTemplateRepo;

  public ShopService(ItemTemplateRepository itemTemplateRepo) {
    this.itemTemplateRepo = itemTemplateRepo;
  }

  public List<Map<String, Object>> generateShopItems(Character character) {
    long today = LocalDate.now().toEpochDay();
    long seed = character.getId() * 100000L + today;
    Random rng = ItemLogic.createRandom(seed);

    List<ItemTemplate> allTemplates = itemTemplateRepo.findAll();
    if (allTemplates.isEmpty()) return Collections.emptyList();

    List<ItemTemplate> shuffled = new ArrayList<>(allTemplates);
    Collections.shuffle(shuffled, rng);

    int count = Math.min(6, shuffled.size());
    List<Map<String, Object>> result = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      ItemTemplate template = shuffled.get(i);

      long itemSeed = seed + i * 999L;
      Random itemRng = ItemLogic.createRandom(itemSeed);
      int statCount = ItemLogic.rollStatCount(itemRng);

      Map<String, Integer> stats = new LinkedHashMap<>();
      for (int j = 0; j < statCount; j++) {
        StatType type = ItemLogic.rollStatType(itemRng);
        int value = ItemLogic.rollStatValue(itemRng, character.getLevel());
        stats.put(type.name(), value);
      }

      int price = (5 + statCount * 10) * character.getLevel();

      Map<String, Object> item = new LinkedHashMap<>();
      item.put("templateId", template.getId());
      item.put("name", template.getName());
      item.put("sprite", template.getSprite());
      item.put("type", template.getType().name());
      item.put("stats", stats);
      item.put("price", price);
      result.add(item);
    }
    return result;
  }
}
