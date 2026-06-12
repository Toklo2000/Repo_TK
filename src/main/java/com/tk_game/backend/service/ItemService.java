package com.tk_game.backend.service;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.*;
import com.tk_game.backend.repository.*;
import com.tk_game.backend.enume.EquipmentSlot;
import com.tk_game.backend.gamelayer.ItemLogic;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class ItemService {

  private final ItemTemplateRepository templateRepo;
  private final ItemInstanceRepository instanceRepo;
  private final ItemStatRepository statRepo;
  private final CharacterRepository characterRepo;

  public ItemService(
      ItemTemplateRepository templateRepo,
      ItemInstanceRepository instanceRepo,
      ItemStatRepository statRepo,
      CharacterRepository characterRepo
  ) {
    this.templateRepo = templateRepo;
    this.instanceRepo = instanceRepo;
    this.statRepo = statRepo;
    this.characterRepo = characterRepo;
  }

  public ItemInstance createItem(Long templateId, Long characterId, int level) {

    ItemTemplate template = templateRepo.findById(templateId)
        .orElseThrow(() -> new RuntimeException("Template not found"));

    Character character = characterRepo.findById(characterId)
        .orElseThrow(() -> new RuntimeException("Character not found"));

    ItemInstance item = new ItemInstance();
    item.setTemplate(template);
    item.setOwner(character);
    item.setSlot(EquipmentSlot.UNEQUIPPED);

    ItemInstance saved = instanceRepo.save(item);

    rollStats(saved, level);

    return saved;
  }

  public void equipItem(Long itemId) {

    ItemInstance item = instanceRepo.findById(itemId)
        .orElseThrow(() -> new RuntimeException("Item not found"));

    Character owner = item.getOwner();

    EquipmentSlot targetSlot = item.getTemplate().getType();

    List<ItemInstance> equipped = instanceRepo.findByOwner(owner);

    for (ItemInstance it : equipped) {
      if (it.getSlot() == targetSlot) {
        it.setSlot(EquipmentSlot.UNEQUIPPED);
        instanceRepo.save(it);
      }
    }

    item.setSlot(targetSlot);
    instanceRepo.save(item);
  }

  public void unequipItem(Long itemId) {
    ItemInstance item = instanceRepo.findById(itemId)
        .orElseThrow(() -> new RuntimeException("Item not found"));

    item.setSlot(EquipmentSlot.UNEQUIPPED);
    instanceRepo.save(item);
  }
  private void rollStats(ItemInstance item, int level) {
    long seed = item.getId() * 31L;
    Random r = ItemLogic.createRandom(seed);

    int statCount = ItemLogic.rollStatCount(r);

    for (int i = 0; i < statCount; i++) {
      ItemStat stat = new ItemStat();
      stat.setItemInstance(item);
      stat.setType(ItemLogic.rollStatType(r));
      stat.setValue(ItemLogic.rollStatValue(r, level));

      statRepo.save(stat);
    }
  }

}
