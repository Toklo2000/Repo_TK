package com.tk_game.backend.controller;

import com.tk_game.backend.model.ItemInstance;
import com.tk_game.backend.model.ItemStat;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.service.ItemService;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.ItemStatRepository; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/items")
public class ItemController {

  private final ItemService itemService;
  private final CharacterRepository characterRepo;
  private final ItemStatRepository itemStatRepo; 

  public ItemController(ItemService itemService, CharacterRepository characterRepo, ItemStatRepository itemStatRepo) {
    this.itemService = itemService;
    this.characterRepo = characterRepo;
    this.itemStatRepo = itemStatRepo;
  }

  @GetMapping("/{characterId}")
  public ResponseEntity<List<Map<String, Object>>> getItems(@PathVariable Long characterId) {
    Character c = characterRepo.findById(characterId).orElseThrow();
    List<ItemInstance> instances = itemService.getItems(c);
    
    List<Map<String, Object>> responseList = new ArrayList<>();
    for (ItemInstance i : instances) {
      Map<String, Object> dto = new HashMap<>();
      dto.put("id", i.getId());
      dto.put("slot", i.getSlot() != null ? i.getSlot().toString() : "UNEQUIPPED");
      dto.put("name", i.getTemplate() != null ? i.getTemplate().getName() : "Unknown Item");
      
      List<ItemStat> stats = itemStatRepo.findByItemInstance(i);
      Map<String, Integer> statsMap = new HashMap<>();
      for (ItemStat stat : stats) {
        if (stat.getType() != null) {
          statsMap.put(stat.getType().name(), stat.getValue());
        }
      }
      dto.put("stats", statsMap); 
      
      responseList.add(dto);
    }
    
    return ResponseEntity.ok(responseList);
  }

  @PostMapping("/equip")
  public void equip(@RequestParam Long itemId) {
    itemService.equipItem(itemId);
  }

  @PostMapping("/unequip")
  public void unequip(@RequestParam Long itemId) {
    itemService.unequipItem(itemId);
  }
}
