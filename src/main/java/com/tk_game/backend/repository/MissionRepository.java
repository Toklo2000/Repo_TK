package com.tk_game.backend.repository;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    @Query("SELECT m FROM Mission m WHERE m.character.id = :characterId AND m.completed = false AND m.startTime > 0")
    Optional<Mission> findActiveByCharacter(@Param("characterId") Long characterId);
}
