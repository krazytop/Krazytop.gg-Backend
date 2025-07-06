package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.riot.RIOTSummoner;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLParticipant {

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
    private Boolean gameEndedInEarlySurrender;
    @JsonAlias("perks")
    private LOLRunes runes;
    @JsonAlias("championId")
    private String champion;
    private String item0;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String ward;
    private RIOTSummoner summoner = new RIOTSummoner();
    @JsonAlias("summoner1Id")
    private String summonerSpell1;
    @JsonAlias("summoner2Id")
    private String summonerSpell2;
    @Transient
    private Integer placement;
    private String augment1;
    private String augment2;
    private String augment3;
    private String augment4;
    private String augment5;
    private String augment6;
    @Transient
    @JsonAlias("playerSubteamId")
    private String subTeamId;
    @Transient
    private String teamId;

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

    @JsonProperty("playerAugment1")
    private void unpackAugment1(String node) {
        if (!Objects.equals(node, "0")) this.augment1 = node;
    }

    @JsonProperty("playerAugment2")
    private void unpackAugment2(String node) {
        if (!Objects.equals(node, "0")) this.augment2 = node;
    }

    @JsonProperty("playerAugment3")
    private void unpackAugment3(String node) {
        if (!Objects.equals(node, "0")) this.augment3 = node;
    }

    @JsonProperty("playerAugment4")
    private void unpackAugment4(String node) {
        if (!Objects.equals(node, "0")) this.augment4 = node;
    }

    @JsonProperty("playerAugment5")
    private void unpackAugment5(String node) {
        if (!Objects.equals(node, "0")) this.augment5 = node;
    }

    @JsonProperty("playerAugment6")
    private void unpackAugment6(String node) {
        if (!Objects.equals(node, "0")) this.augment6 = node;
    }

    @JsonProperty("item0")
    private void unpackitem0(String node) {
        if (!Objects.equals(node, "0")) this.item0 = node;
    }

    @JsonProperty("item1")
    private void unpackitem1(String node) {
        if (!Objects.equals(node, "0")) this.item1 = node;
    }

    @JsonProperty("item2")
    private void unpackitem2(String node) {
        if (!Objects.equals(node, "0")) this.item2 = node;
    }

    @JsonProperty("item3")
    private void unpackitem3(String node) {
        if (!Objects.equals(node, "0")) this.item3 = node;
    }

    @JsonProperty("item4")
    private void unpackitem4(String node) {
        if (!Objects.equals(node, "0")) this.item4 = node;
    }

    @JsonProperty("item5")
    private void unpackitem5(String node) {
        if (!Objects.equals(node, "0")) this.item5 = node;
    }

    @JsonProperty("item6")
    private void unpackWard(String node) {
        if (!Objects.equals(node, "0")) this.ward = node;
    }

    @JsonProperty("individualPosition")
    private void unpackRole(String role) {
        this.role = role;
    }
}
