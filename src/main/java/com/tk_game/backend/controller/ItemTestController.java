package com.tk_game.backend.controller;

import com.tk_game.backend.model.ItemInstance;
import com.tk_game.backend.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/items")
public class ItemTestController {

    private final ItemService itemService;

    public ItemTestController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/generate-random")
    public ResponseEntity<String> generateRandomItemTest(
            @RequestParam Long templateId, 
            @RequestParam Long characterId, 
            @RequestParam int itemLevel) {
        try {
            itemService.createItem(templateId, characterId, itemLevel);
            return ResponseEntity.ok("SUCCESS");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
