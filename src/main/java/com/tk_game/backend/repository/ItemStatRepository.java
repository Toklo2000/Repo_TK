package com.tk_game.backend.repository;

import com.tk_game.backend.model.ItemStat;
import com.tk_game.backend.model.ItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemStatRepository extends JpaRepository<ItemStat, Long> {

  List<ItemStat> findByItemInstance(ItemInstance itemInstance);
}
