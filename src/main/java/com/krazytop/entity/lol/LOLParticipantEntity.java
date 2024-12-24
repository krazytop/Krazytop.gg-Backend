package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLParticipantEntity {

    private Integer champLevel;
    private Integer kills;
    private Integer assists;
    private Integer deaths;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
    private Integer visionScore;
    @JsonAlias("totalMinionsKilled")
    private Integer minions;
    @JsonAlias("neutralMinionsKilled")
    private Integer neutralMinions;
    private Integer doubleKills;
    private Integer tripleKills;
    private Integer quadraKills;
    private Integer pentaKills;
    private Integer physicalDamageDealtToChampions;
    private Integer magicDamageDealtToChampions;
    private Integer trueDamageDealtToChampions;
    private Integer physicalDamageTaken;
    private Integer magicDamageTaken;
    private Integer trueDamageTaken;
    @JsonAlias("goldEarned")
    private Integer golds;
    private boolean gameEndedInEarlySurrender;
    @JsonAlias("perks")
    private LOLRunesEntity runes;
    @JsonAlias("championId")
    private String champion;
    private String item0;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    @JsonAlias("item6")
    private String ward;
    private RIOTSummonerEntity summoner = new RIOTSummonerEntity();
    @JsonAlias("summoner1Id")
    private String summonerSpell1;
    @JsonAlias("summoner2Id")
    private String summonerSpell2;
    @Transient
    private Integer placement;
    @JsonAlias("playerAugment1")
    private String augment1;
    @JsonAlias("playerAugment2")
    private String augment2;
    @JsonAlias("playerAugment3")
    private String augment3;
    @JsonAlias("playerAugment4")
    private String augment4;
    @JsonAlias("playerAugment5")
    private String augment5;
    @JsonAlias("playerAugment6")
    private String augment6;
    @Transient
    @JsonAlias("playerSubteamId")
    private String subTeamId;
    @Transient
    private String teamId;

    @JsonProperty("summonerId")
    private void unpackId(String id) {
        this.getSummoner().setId(id);
    }

    @JsonProperty("puuid")
    private void unpackPuuid(String puuid) {
        this.getSummoner().setPuuid(puuid);
    }

    @JsonProperty("riotIdGameName")
    private void unpackName(String name) {
        this.getSummoner().setName(name);
    }

    @JsonProperty("riotIdTagline")
    private void unpackTag(String tag) {
        this.getSummoner().setTag(tag);
    }

    @JsonProperty("summonerLevel")
    private void unpackLevel(int level) {
        this.getSummoner().setLevel(level);
    }

    @JsonProperty("profileIcon")
    private void unpackIcon(int icon) {
        this.getSummoner().setIcon(icon);
    }


    @JsonProperty("individualPosition")
    private void unpackRole(String role) {
        this.role = role;
    }
}
