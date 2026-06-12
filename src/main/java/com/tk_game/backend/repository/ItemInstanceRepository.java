package com.tk_game.backend.repository;

import com.tk_game.backend.model.ItemInstance;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.enume.EquipmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemInstanceRepository extends JpaRepository<ItemInstance, Long> {
  List<ItemInstance> findByOwner(Character owner);
  Optional<ItemInstance> findByOwnerAndSlot(Character owner, EquipmentSlot slot);
}
