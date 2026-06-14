package com.tk_game.backend.controller;
import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.Mission;
import com.tk_game.backend.model.TavernOffer;
import com.tk_game.backend.repository.AccountRepository;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.MissionRepository;
import com.tk_game.backend.service.TavernService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tavern")
public class TavernController {
    private final TavernService tavernService;
    private final AccountRepository accountRepository;
    private final CharacterRepository characterRepository;
    private final MissionRepository missionRepository;

    public TavernController(TavernService tavernService,
                            AccountRepository accountRepository,
                            CharacterRepository characterRepository,
                            MissionRepository missionRepository) {
        this.tavernService = tavernService;
        this.accountRepository = accountRepository;
        this.characterRepository = characterRepository;
        this.missionRepository = missionRepository;
    }

    private Character getCharacter(UserDetails userDetails) {
        return accountRepository.findByLogin(userDetails.getUsername())
            .flatMap(account -> characterRepository.findByAccount_Id(account.getId()))
            .orElseThrow(() -> new RuntimeException("Character not found"));
    }

    @GetMapping("/offers")
    public List<TavernOffer> getOffers(@AuthenticationPrincipal UserDetails userDetails) {
        Character character = getCharacter(userDetails);
        return tavernService.getOffers(character);
    }

    @PostMapping("/accept/{offerId}")
    public Mission acceptOffer(@PathVariable Long offerId,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Character character = getCharacter(userDetails);
        return tavernService.acceptOffer(offerId, character);
    }

    @PostMapping("/complete")
    public void completeQuest(@RequestParam boolean success,
                              @AuthenticationPrincipal UserDetails userDetails) {
        Character character = getCharacter(userDetails);
        tavernService.completeQuest(character, success);
    }

    @GetMapping("/mission/active")
    public ResponseEntity<Mission> getActiveMission(@AuthenticationPrincipal UserDetails userDetails) {
        Character character = getCharacter(userDetails);
        return missionRepository.findActiveByCharacter(character.getId())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
