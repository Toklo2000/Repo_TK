package com.tk_game.backend.repository;

import com.tk_game.backend.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
  Character findByAccount_Id(Long accountId);
}
