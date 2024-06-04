package com.krazytop.service.clash_royal;

import com.krazytop.api.clash_royal.CRNomenclatureApi;
import com.krazytop.api.clash_royal.CRPlayerApi;
import com.krazytop.entity.clash_royal.*;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CRPlayerService {

    private final CRPlayerApi crPlayerApi;
    private final CRApiService crApiService;
    private final CRNomenclatureApi crNomenclatureApi;

    @Autowired
    public CRPlayerService(CRPlayerApi crPlayerApi, CRApiService crApiService, CRNomenclatureApi crNomenclatureApi) {
        this.crPlayerApi = crPlayerApi;
        this.crApiService = crApiService;
        this.crNomenclatureApi = crNomenclatureApi;
    }

    public CRPlayerEntity getLocalPlayer(String playerId) {
        return crPlayerApi.getPlayer(playerId);
    }

    public CRPlayerEntity updateRemoteToLocalPlayer(String playerId) {
        CRPlayerEntity remotePlayer = getRemotePlayer(playerId);
        accountLevelEnrichment(remotePlayer);
        leaguesEnrichment(remotePlayer);
        chestsEnrichment(remotePlayer);
        badgeEnrichment(remotePlayer);
        return crPlayerApi.updatePlayer(remotePlayer);
    }

    public CRPlayerEntity getRemotePlayer(String playerId) {
        String apiUrl = CRPlayerEntity.getUrl("%23" + playerId);
        CRPlayerEntity player = crApiService.callCrApi(apiUrl, CRPlayerEntity.class);
        if (player != null) {
            cardEnrichment(player);
            player.setId(player.getId().replace("#", ""));
        }
        return player;
    }

    private void accountLevelEnrichment(CRPlayerEntity player) {
        CRAccountLevelEntity accountLevel = new ModelMapper().map(crNomenclatureApi.getAccountLevelNomenclature(player.getExpLevel()), CRAccountLevelEntity.class);
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
            CRCardNomenclature cardNomenclature = crNomenclatureApi.getCardNomenclature(card.getId());
            if (cardNomenclature != null) { //TODO maj des cartes au fil du temps
                CRCardRarityNomenclature rarity = crNomenclatureApi.getCardRarityNomenclature(cardNomenclature.getRarity());
                card.setUpgradeCost(rarity.getUpgradeCost().get(card.getLevel() -1));
                card.setLevel(card.getLevel() + rarity.getRelativeLevel());
                new ModelMapper().map(cardNomenclature, card);
            }
        }
    }

    private void chestsEnrichment(CRPlayerEntity player) {
        String apiUrl = CRUpcomingChestsEntity.getUrl("%23" + player.getId());
        CRUpcomingChestsEntity chest = crApiService.callCrApi(apiUrl, CRUpcomingChestsEntity.class);
        player.setUpcomingChests(chest.getChests());
    }

    private void badgeEnrichment(CRPlayerEntity player) {
        for (CRBadgeEntity badge : player.getBadges()) {
            badge.setImage(badge.getIconUrls().getImage());
        }
    }

}
