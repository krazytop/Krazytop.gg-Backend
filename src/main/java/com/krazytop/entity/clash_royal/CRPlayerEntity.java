package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("expLevel")
    @Transient
    private int expLevel;

    @JsonProperty("starPoints")
    private int starPoints;

    @JsonProperty("expPoints")
    @Transient
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

    @JsonProperty("arena")
    private CRArenaEntity arena;

    @JsonProperty("badges")
    private List<CRBadgeEntity> badges;

    @JsonProperty("cards")
    private List<CRCardEntity> cards;

    @JsonProperty("currentDeck")
    private List<CRCardEntity> currentDeck;

    @JsonProperty("currentFavouriteCard")
    private CRCardEntity currentFavouriteCard;

    @JsonAlias("leagueStatistics")
    private CRTrophiesEntity seasonsTrophies;

    @Transient
    @JsonProperty("currentPathOfLegendSeasonResult")
    private CRLeagueEntity currentPathOfLegendSeasonResult;

    @Transient
    @JsonProperty("lastPathOfLegendSeasonResult")
    private CRLeagueEntity lastPathOfLegendSeasonResult;

    @Transient
    @JsonProperty("bestPathOfLegendSeasonResult")
    private CRLeagueEntity bestPathOfLegendSeasonResult;

    private Date updateDate;
    private CRAccountLevelEntity accountLevel;
    private CRLeaguesEntity seasonsLeagues;
    private List<CRChestEntity> upcomingChests;

    public static String getUrl(String playerId) {
        return "https://api.clashroyale.com/v1/players/" + playerId;
    }

    public String toString() {
        return this.id;
    }
}
