package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRArenaNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRArenaNomenclatureRepository;
import com.krazytop.repository.lol.LOLQueueNomenclatureRepository;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Player")
public class CRPlayerEntity {

    @JsonAlias("tag")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private CRAccountLevelNomenclature accountLevelNomenclature;
    @JsonProperty("starPoints")
    private int starPoints;
    @JsonProperty("expPoints")
    private int expPoints;
    @JsonProperty("bestTrophies")
    private int bestTrophies;
    @JsonProperty("wins")
    private int wins;
    @JsonProperty("losses")
    private int losses;
    @JsonProperty("threeCrownWins")
    private int threeCrownWins;
    @JsonProperty("challengeCardsWon")
    private int challengeCardsWon;
    @JsonProperty("clanCardsCollected")
    private int clanWarCardsWon;
    @JsonProperty("totalDonations")
    private int totalDonations;
    @JsonProperty("clan")
    private CRClanEntity clan;
    @JsonIgnore
    private CRArenaNomenclature arena;
    @JsonProperty("badges")
    private List<CRBadgeEntity> badges;
    @JsonProperty("cards")
    private List<CRCardEntity> cards;
    @JsonProperty("currentDeck")
    private List<CRCardEntity> currentDeck;
    @JsonProperty("currentFavouriteCard")
    private CRCardEntity currentFavouriteCard;
    @JsonProperty("leagueStatistics")
    private CRTrophiesEntity seasonsTrophies;
    @JsonIgnore
    private Date updateDate;
    @JsonIgnore
    private List<CRChestEntity> upcomingChests;
    @JsonIgnore
    private CRLeaguesEntity seasonsLeagues = new CRLeaguesEntity();

    @JsonProperty("arena")
    private void unpackArena(JsonNode node) {
        CRArenaNomenclatureRepository arenaNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(CRArenaNomenclatureRepository.class);
        this.arena = arenaNomenclatureRepository.findFirstById(node.asInt());
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
