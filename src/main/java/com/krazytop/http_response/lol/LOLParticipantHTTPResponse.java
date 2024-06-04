package com.krazytop.http_response.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLParticipantHTTPResponse {

    @JsonProperty("champLevel")
    private int champLevel;

    @JsonProperty("championId")
    private int championId;

    @JsonProperty("kills")
    private int kills;

    @JsonProperty("assists")
    private int assists;

    @JsonProperty("deaths")
    private int deaths;

    @JsonProperty("individualPosition")
    private String role;

    @JsonProperty("item0")
    private int item0Id;

    @JsonProperty("item1")
    private int item1Id;

    @JsonProperty("item2")
    private int item2Id;

    @JsonProperty("item3")
    private int item3Id;

    @JsonProperty("item4")
    private int item4Id;

    @JsonProperty("item5")
    private int item5Id;

    @JsonProperty("item6")
    private int wardId;

    @JsonProperty("puuid")
    private String puuid;

    @JsonProperty("summonerId")
    private String summonerId;

    @JsonProperty("riotIdGameName")
    private String riotIdGameName;

    @JsonProperty("riotIdTagline")
    private String riotIdTagline;

    @JsonProperty("summonerLevel")
    private String summonerLevel;

    @JsonProperty("profileIcon")
    private String profileIcon;

    @JsonProperty("teamId")
    private String teamId;

    @JsonProperty("visionScore")
    private int visionScore;

    @JsonProperty("totalMinionsKilled")
    private int minions;

    @JsonProperty("doubleKills")
    private int doubleKills;

    @JsonProperty("tripleKills")
    private int tripleKills;

    @JsonProperty("quadraKills")
    private int quadraKills;

    @JsonProperty("pentaKills")
    private int pentaKills;

    @JsonProperty("physicalDamageDealtToChampions")
    private int physicalDamageDealtToChampions;

    @JsonProperty("magicDamageDealtToChampions")
    private int magicDamageDealtToChampions;

    @JsonProperty("trueDamageDealtToChampions")
    private int trueDamageDealtToChampions;

    @JsonProperty("physicalDamageTaken")
    private int physicalDamageTaken;

    @JsonProperty("magicDamageTaken")
    private int magicDamageTaken;

    @JsonProperty("trueDamageTaken")
    private int trueDamageTaken;

    @JsonProperty("summoner1Id")
    private String summonerSpellId1;

    @JsonProperty("summoner2Id")
    private String summonerSpellId2;

    @JsonProperty("gameEndedInEarlySurrender")
    private boolean gameEndedInEarlySurrender;

    @JsonProperty("goldEarned")
    private int golds;

    @JsonProperty("perks")
    private Perks perks;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Perks {

        @JsonProperty("styles")
        private List<RuneStyle> styles;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RuneStyle {

            @JsonProperty("selections")
            private List<RuneSelection> selections;

            @JsonProperty("style")
            private String styleId;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class RuneSelection {

            @JsonProperty("perk")
            private String perkId;
        }
    }

}
