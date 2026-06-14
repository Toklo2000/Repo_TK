package com.tk_game.backend.repository;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.ItemInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemInstanceRepository extends JpaRepository<ItemInstance, Long> {
    List<ItemInstance> findByCharacter(Character character); 
}
