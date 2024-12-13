package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRArenaNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRArenaNomenclatureRepository;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Player")
public class CRPlayerEntity {

    @JsonAlias("tag")
    private String id;
    private String name;
    private CRAccountLevelNomenclature accountLevelNomenclature;
    private int starPoints;
    private int expPoints;
    private int bestTrophies;
    private int wins;
    private int losses;
    private int threeCrownWins;
    private int challengeCardsWon;
    @JsonAlias("clanCardsCollected")
    private int clanWarCardsWon;
    private int totalDonations;
    private CRClanEntity clan;
    private CRArenaNomenclature arenaNomenclature;
    private List<CRBadgeEntity> badges;
    private List<CRCardEntity> cards;
    private List<CRCardEntity> currentDeck;
    private CRCardEntity currentFavouriteCard;
    @JsonAlias("leagueStatistics")
    private CRTrophiesEntity seasonsTrophies;
    private Date updateDate;
    private List<CRChestEntity> upcomingChests;
    private CRLeaguesEntity seasonsLeagues = new CRLeaguesEntity();

    @JsonProperty("currentFavouriteCard")
    private void retrieveCurrentFavoriteCard(JsonNode node) {
        Optional<CRCardEntity> nomenclature = this.cards.stream().filter(card -> card.getNomenclature().getId() == node.get("id").asInt()).findFirst();
        nomenclature.ifPresent(crCardEntity -> this.currentFavouriteCard = crCardEntity);
    }

    @JsonProperty("arena")
    private void unpackArena(JsonNode node) {
        CRArenaNomenclatureRepository arenaNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(CRArenaNomenclatureRepository.class);
        this.arenaNomenclature = arenaNomenclatureRepository.findFirstById(node.get("id").asInt());
    }

    @JsonProperty("currentPathOfLegendSeasonResult")
    private void unpackCurrentLeague(JsonNode node) {
        this.seasonsLeagues.setCurrentSeason(new ObjectMapper().convertValue(node, CRLeagueEntity.class));
    }

    @JsonProperty("lastPathOfLegendSeasonResult")
    private void unpackPreviousLeague(JsonNode node) {
        this.seasonsLeagues.setPreviousSeason(new ObjectMapper().convertValue(node, CRLeagueEntity.class));
    }

    @JsonProperty("bestPathOfLegendSeasonResult")
    private void unpackBestLeague(JsonNode node) {
        this.seasonsLeagues.setBestSeason(new ObjectMapper().convertValue(node, CRLeagueEntity.class));
    }

    @JsonProperty("expLevel")
    private void unpackAccountLevel(JsonNode node) {
        CRAccountLevelNomenclatureRepository accountLevelNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(CRAccountLevelNomenclatureRepository.class);
        this.setAccountLevelNomenclature(accountLevelNomenclatureRepository.findFirstByLevel(node.asInt()));
    }
}
