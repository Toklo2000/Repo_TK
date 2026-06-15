package com.tk_game.backend.controller;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.service.ShopService;
import com.tk_game.backend.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop")
public class ShopController {

  private final ShopService shopService;
  private final ItemService itemService;
  private final CharacterRepository characterRepo;

  public ShopController(ShopService shopService, ItemService itemService, CharacterRepository characterRepo) {
    this.shopService = shopService;
    this.itemService = itemService;
    this.characterRepo = characterRepo;
  }

  @GetMapping("/{characterId}")
  public ResponseEntity<List<Map<String, Object>>> getShop(@PathVariable Long characterId, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "6") int count) {
    Character character = characterRepo.findById(characterId).orElseThrow(() -> new RuntimeException("Character not found"));
    List<Map<String, Object>> items = shopService.generateShopItems(character, offset, count);
    return ResponseEntity.ok(items);
  }

  @PostMapping("/buy")
  public ResponseEntity<Map<String, Object>> buyItem(@RequestParam Long characterId, @RequestParam Long templateId, @RequestParam int price, @RequestParam long itemSeed) {
    Character character = characterRepo.findById(characterId).orElseThrow(() -> new RuntimeException("Character not found"));
    if (character.getGold() < price) {
      return ResponseEntity.badRequest().body(Map.of("error", "Za mało złota!"));
    }
    character.setGold(character.getGold() - price);
    characterRepo.save(character);
    itemService.createItemWithSeed(templateId, characterId, character.getLevel(), itemSeed);
    return ResponseEntity.ok(Map.of("success", true, "gold", character.getGold()));
  }
}
