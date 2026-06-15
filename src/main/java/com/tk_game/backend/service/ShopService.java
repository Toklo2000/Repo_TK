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
    return generateShopItems(character, 0, 6);
  }

  public List<Map<String, Object>> generateShopItems(Character character, int offset, int count) {
    long today = LocalDate.now().toEpochDay();
    long baseSeed = character.getId() * 100000L + today;

    List<ItemTemplate> allTemplates = itemTemplateRepo.findAll();
    if (allTemplates.isEmpty()) return Collections.emptyList();

    List<Map<String, Object>> result = new ArrayList<>();

    for (int i = offset; i < offset + count; i++) {
      long itemSeed = baseSeed + i * 999L;
      Random rng = ItemLogic.createRandom(itemSeed);

      List<ItemTemplate> shuffled = new ArrayList<>(allTemplates);
      Collections.shuffle(shuffled, ItemLogic.createRandom(baseSeed + i * 7L));
      ItemTemplate template = shuffled.get(0);

      int statCount = ItemLogic.rollStatCount(rng);
      Map<String, Integer> stats = new LinkedHashMap<>();
      for (int j = 0; j < statCount; j++) {
        StatType type = ItemLogic.rollStatType(rng);
        int value = ItemLogic.rollStatValue(rng, character.getLevel());
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
      item.put("itemSeed", itemSeed);
      item.put("index", i);
      result.add(item);
    }

    return result;
  }
}
