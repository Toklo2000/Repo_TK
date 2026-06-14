package com.tk_game.backend.repository;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.TavernOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface TavernOfferRepository extends JpaRepository<TavernOffer, Long> {
    List<TavernOffer> findByCharacter(Character character);
    @Transactional
    void deleteByCharacter(Character character);
}
