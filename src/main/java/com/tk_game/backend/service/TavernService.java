package com.tk_game.backend.service;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.model.Mission;
import com.tk_game.backend.model.QuestTemplate;
import com.tk_game.backend.model.TavernOffer;
import com.tk_game.backend.repository.CharacterRepository;
import com.tk_game.backend.repository.MissionRepository;
import com.tk_game.backend.repository.QuestTemplateRepository;
import com.tk_game.backend.repository.TavernOfferRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TavernService {

    private static final String[] WORD1 = {
        "Mroczny", "Zaklęty", "Starożytny", "Przeklęty", "Dziki",
        "Zapomniany", "Krwawy", "Plugawy", "Zagubiony", "Tajemny",
        "Zepsuty", "Wieczny", "Lodowy", "Ognisty", "Cienisty"
    };
    private static final String[] WORD2 = {
        "Las", "Zamek", "Grób", "Szczyt", "Bagno",
        "Tunel", "Wąwóz", "Klasztor", "Wieża", "Ruiny",
        "Ołtarz", "Labirynt", "Most", "Tron", "Skarbiec"
    };
    private static final String[] WORD3 = {
        "Potępieńców", "Smoków", "Cieni", "Demonów", "Umarłych",
        "Czarownic", "Gigantów", "Wampirów", "Wilkołaków", "Szkieletów",
        "Goblinów", "Trolli", "Wiedźm", "Duchów", "Orków"
    };
    private static final String[] DESCRIPTIONS = {
        "Potwory terroryzują okoliczne wioski. Mieszkańcy błagają o pomoc.",
        "Starożytne zło przebudziło się. Tylko dzielny bohater może je powstrzymać.",
        "Złodzieje ukradli relikwię. Odzyskaj ją za wszelką cenę.",
        "Tajemnicze zaginięcia niepokoją mieszkańców. Zbadaj sprawę.",
        "Kult ciemności przeprowadza rytuały. Przeszkódź im.",
        "Potwór strzeże skarbu. Pokonaj go i przywłaszcz nagrodę.",
        "Droga handlowa jest zablokowana. Oczyść ją z bandytów.",
        "Wróg nadciąga od północy. Zatrzymaj jego awangardę.",
        "Magiczny artefakt działa nieprzewidywalnie. Zniszcz go.",
        "Starożytna wróżba musi się wypełnić. Ty jesteś wybrańcem."
    };

    private final MissionRepository missionRepository;
    private final QuestTemplateRepository questTemplateRepository;
    private final TavernOfferRepository tavernOfferRepository;
    private final CharacterRepository characterRepository;

    public TavernService(MissionRepository missionRepository,
                         QuestTemplateRepository questTemplateRepository,
                         TavernOfferRepository tavernOfferRepository,
                         CharacterRepository characterRepository) {
        this.missionRepository = missionRepository;
        this.questTemplateRepository = questTemplateRepository;
        this.tavernOfferRepository = tavernOfferRepository;
        this.characterRepository = characterRepository;
    }

    public List<TavernOffer> getOffers(Character character) {
        List<TavernOffer> existing = tavernOfferRepository.findByCharacter(character);
        if (!existing.isEmpty()) return existing;
        return generateOffers(character);
    }

    public List<TavernOffer> generateOffers(Character character) {
        tavernOfferRepository.deleteByCharacter(character);

        int questLevel = getQuestLevel(character.getLevel());
        long seed = character.getSeed() + character.getQuestsCompleted();
        Random rng = new Random(seed);

        List<TavernOffer> offers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            QuestTemplate qt = new QuestTemplate();
            qt.setName(WORD1[rng.nextInt(15)] + " " + WORD2[rng.nextInt(15)] + " " + WORD3[rng.nextInt(15)]);
            qt.setDescription(DESCRIPTIONS[rng.nextInt(10)]);
            qt.setLevel(questLevel);
            qt.setExpReward(5 * questLevel);
            qt.setGoldReward(10 * questLevel);
            qt.setDuration(5 * questLevel);
            questTemplateRepository.save(qt);

            Mission mission = new Mission();
            mission.setCharacter(character);
            mission.setQuestTemplate(qt);
            mission.setCompleted(false);
            mission.setSuccess(false);
            missionRepository.save(mission);

            TavernOffer offer = new TavernOffer();
            offer.setCharacter(character);
            offer.setMission(mission);
            tavernOfferRepository.save(offer);
            offers.add(offer);
        }
        return offers;
    }

    private int getQuestLevel(int characterLevel) {
        if (characterLevel >= 50) return 50;
        if (characterLevel < 5) return 1;
        return (characterLevel / 5) * 5;
    }

    public Mission acceptOffer(Long offerId, Character character) {
        TavernOffer offer = tavernOfferRepository.findById(offerId)
            .orElseThrow(() -> new RuntimeException("Offer not found"));

        Mission mission = offer.getMission();
        long now = System.currentTimeMillis();
        mission.setStartTime(now);
        mission.setEndTime(now + mission.getQuestTemplate().getDuration() * 1000L);
        return missionRepository.save(mission);
    }

    public void completeQuest(Character character, boolean success) {
        Mission active = missionRepository.findActiveByCharacter(character.getId())
            .orElseThrow(() -> new RuntimeException("No active mission"));

        active.setCompleted(true);
        active.setSuccess(success);
        missionRepository.save(active);

        int exp = (int)(active.getQuestTemplate().getExpReward() * (success ? 1.0 : 0.2));
        int gold = (int)(active.getQuestTemplate().getGoldReward() * (success ? 1.0 : 0.2));

        character.setExp(character.getExp() + exp);
        character.setGold(character.getGold() + gold);
        character.setQuestsCompleted(character.getQuestsCompleted() + 1);
        applyLevelUp(character);
        characterRepository.save(character);

        generateOffers(character);
    }

    private void applyLevelUp(Character character) {
        if (character.getLevel() >= 50) return;
        while (character.getExp() >= expRequired(character.getLevel()) && character.getLevel() < 50) {
            int required = expRequired(character.getLevel());
            character.setExp(character.getExp() - required);
            character.setLevel(character.getLevel() + 1);
        }
    }

    private int expRequired(int level) {
        if (level == 1) return 10;
        if (level == 2) return 15;
        if (level == 3) return 20;
        if (level == 4) return 25;
        if (level == 5) return 50;
        return 25 * (level - 1);
    }
}
