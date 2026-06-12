package com.tk_game.backend.repository;

import com.tk_game.backend.model.Character;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
  Optional<Character> findByAccount_Id(Long accountId);
}
