package com.tk_game.backend.service;

import com.tk_game.backend.enume.EquipmentSlot;
import com.tk_game.backend.enume.StatType;
import com.tk_game.backend.gamelayer.ItemLogic;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.ItemInstance;
import com.tk_game.backend.model.ItemStat;
import com.tk_game.backend.model.ItemTemplate;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.ItemInstanceRepository;
import com.tk_game.backend.repository.ItemTemplateRepository;
import com.tk_game.backend.repository.ItemStatRepository; 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;

@Service
public class ItemService {
  private final ItemInstanceRepository itemInstanceRepo;
  private final ItemTemplateRepository itemTemplateRepo;
  private final CharacterRepository characterRepo;
  private final ItemStatRepository itemStatRepo; 

  public ItemService(ItemInstanceRepository itemInstanceRepo,
                     ItemTemplateRepository itemTemplateRepo,
                     CharacterRepository characterRepo,
                     ItemStatRepository itemStatRepo) {
    this.itemInstanceRepo = itemInstanceRepo;
    this.itemTemplateRepo = itemTemplateRepo;
    this.characterRepo = characterRepo;
    this.itemStatRepo = itemStatRepo;
  }

  @Transactional 
  public ItemInstance createItem(Long templateId, Long characterId, int itemLevel) {
    ItemTemplate template = itemTemplateRepo.findById(templateId)
        .orElseThrow(() -> new RuntimeException("Template not found"));
    Character character = characterRepo.findById(characterId)
        .orElseThrow(() -> new RuntimeException("Character not found"));
    
    ItemInstance item = new ItemInstance();
    item.setTemplate(template);
    item.setCharacter(character); 
    item.setSlot(EquipmentSlot.UNEQUIPPED);
    ItemInstance savedItem = itemInstanceRepo.save(item);

    Random random = new Random(); 
    int statsCount = ItemLogic.rollStatCount(random);

    for (int i = 0; i < statsCount; i++) {
        StatType rolledType = ItemLogic.rollStatType(random);
        int rolledValue = ItemLogic.rollStatValue(random, itemLevel);

        ItemStat itemStat = new ItemStat();
        itemStat.setItemInstance(savedItem); 
        itemStat.setType(rolledType);
        itemStat.setValue(rolledValue);

        itemStatRepo.save(itemStat); 
    }

    return savedItem;
  }

  public List<ItemInstance> getItems(Character character) {
    return itemInstanceRepo.findByCharacter(character);
  }

  public void equipItem(Long itemId) {
    ItemInstance item = itemInstanceRepo.findById(itemId)
        .orElseThrow(() -> new RuntimeException("Item not found"));

    Character character = item.getCharacter(); 
    List<ItemInstance> items = itemInstanceRepo.findByCharacter(character);

    EquipmentSlot targetSlot = item.getTemplate().getType();

    for (ItemInstance i : items) {
      if (i.getSlot() == targetSlot) {
        i.setSlot(EquipmentSlot.UNEQUIPPED);
        itemInstanceRepo.save(i);
      }
    }

    item.setSlot(targetSlot);
    itemInstanceRepo.save(item);
  }

  public void unequipItem(Long itemId) {
    ItemInstance item = itemInstanceRepo.findById(itemId)
        .orElseThrow(() -> new RuntimeException("Item not found"));
    item.setSlot(EquipmentSlot.UNEQUIPPED);
    itemInstanceRepo.save(item);
  }
}
