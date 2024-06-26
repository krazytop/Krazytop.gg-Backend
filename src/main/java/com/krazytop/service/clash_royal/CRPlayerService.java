package com.krazytop.service.clash_royal;

import com.krazytop.entity.clash_royal.*;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardRarityNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRPlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CRPlayerService {

    private final CRApiService apiService;
    private final CRAccountLevelNomenclatureRepository accountLevelNomenclatureRepository;
    private final CRCardNomenclatureRepository cardNomenclatureRepository;
    private final CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;
    private final CRPlayerRepository playerRepository;

    @Autowired
    public CRPlayerService(CRApiService apiService, CRAccountLevelNomenclatureRepository accountLevelNomenclatureRepository, CRCardNomenclatureRepository cardNomenclatureRepository, CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository, CRPlayerRepository playerRepository) {
        this.apiService = apiService;
        this.accountLevelNomenclatureRepository = accountLevelNomenclatureRepository;
        this.cardNomenclatureRepository = cardNomenclatureRepository;
        this.cardRarityNomenclatureRepository = cardRarityNomenclatureRepository;
        this.playerRepository = playerRepository;
    }

    public CRPlayerEntity getLocalPlayer(String playerId) {
        return playerRepository.findFirstById(playerId);
    }

    public CRPlayerEntity updateRemoteToLocalPlayer(String playerId) {
        CRPlayerEntity remotePlayer = getRemotePlayer(playerId);
        accountLevelEnrichment(remotePlayer);
        leaguesEnrichment(remotePlayer);
        chestsEnrichment(remotePlayer);
        badgeEnrichment(remotePlayer);
        remotePlayer.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return playerRepository.save(remotePlayer);
    }

    public CRPlayerEntity getRemotePlayer(String playerId) {
        String apiUrl = CRPlayerEntity.getUrl("%23" + playerId);
        CRPlayerEntity player = apiService.callCrApi(apiUrl, CRPlayerEntity.class);
        if (player != null) {
            cardEnrichment(player);
            player.setId(player.getId().replace("#", ""));
        }
        return player;
    }

    private void accountLevelEnrichment(CRPlayerEntity player) {
        CRAccountLevelEntity accountLevel = new ModelMapper().map(accountLevelNomenclatureRepository.findFirstByLevel(player.getExpLevel()), CRAccountLevelEntity.class);
        accountLevel.setCurrentExp(player.getExpPoints());
        player.setAccountLevel(accountLevel);
    }

    private void leaguesEnrichment(CRPlayerEntity player) {
        CRLeagueEntity currentSeason = new CRLeagueEntity();
        currentSeason.setLeagueNumber(player.getCurrentPathOfLegendSeasonResult().getLeagueNumber());
        currentSeason.setRank(player.getCurrentPathOfLegendSeasonResult().getRank());
        currentSeason.setTrophies(player.getCurrentPathOfLegendSeasonResult().getTrophies());
        CRLeagueEntity previousSeason = new CRLeagueEntity();
        previousSeason.setLeagueNumber(player.getLastPathOfLegendSeasonResult().getLeagueNumber());
        previousSeason.setRank(player.getLastPathOfLegendSeasonResult().getRank());
        previousSeason.setTrophies(player.getLastPathOfLegendSeasonResult().getTrophies());
        CRLeagueEntity bestSeason = new CRLeagueEntity();
        bestSeason.setLeagueNumber(player.getBestPathOfLegendSeasonResult().getLeagueNumber());
        bestSeason.setRank(player.getBestPathOfLegendSeasonResult().getRank());
        bestSeason.setTrophies(player.getBestPathOfLegendSeasonResult().getTrophies());
        CRLeaguesEntity leagues = new CRLeaguesEntity();
        leagues.setCurrentSeason(currentSeason);
        leagues.setPreviousSeason(previousSeason);
        leagues.setBestSeason(bestSeason);
        player.setSeasonsLeagues(leagues);
    }

    private void cardEnrichment(CRPlayerEntity player) {
        for (CRCardEntity card : player.getCards()) {
            CRCardNomenclature cardNomenclature = cardNomenclatureRepository.findFirstById(card.getId());
            if (cardNomenclature != null) { //TODO maj des cartes au fil du temps
                CRCardRarityNomenclature rarity = cardRarityNomenclatureRepository.findFirstByName(cardNomenclature.getRarity());
                card.setUpgradeCost(rarity.getUpgradeCost().get(card.getLevel() -1));
                card.setLevel(card.getLevel() + rarity.getRelativeLevel());
                new ModelMapper().map(cardNomenclature, card);
            }
        }
    }

    private void chestsEnrichment(CRPlayerEntity player) {
        String apiUrl = CRUpcomingChestsEntity.getUrl("%23" + player.getId());
        CRUpcomingChestsEntity chest = apiService.callCrApi(apiUrl, CRUpcomingChestsEntity.class);
        player.setUpcomingChests(chest.getChests());
    }

    private void badgeEnrichment(CRPlayerEntity player) {
        for (CRBadgeEntity badge : player.getBadges()) {
            badge.setImage(badge.getIconUrls().getImage());
        }
    }

}
