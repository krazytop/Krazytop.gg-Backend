package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import com.krazytop.nomenclature.lol.LOLItemNomenclature;
import com.krazytop.nomenclature.lol.LOLSummonerSpellNomenclature;
import com.krazytop.repository.lol.LOLChampionNomenclatureRepository;
import com.krazytop.repository.lol.LOLItemNomenclatureRepository;
import com.krazytop.repository.lol.LOLSummonerSpellNomenclatureRepository;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.function.Consumer;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLParticipantEntity {

    private int champLevel;
    private int kills;
    private int assists;
    private int deaths;
    @JsonIgnore
    private String role;//TODO
    private int visionScore;
    @JsonAlias("totalMinionsKilled")
    private int minions;
    @JsonAlias("neutralMinionsKilled")
    private int neutralMinions;
    private int doubleKills;
    private int tripleKills;
    private int quadraKills;
    private int pentaKills;
    private int physicalDamageDealtToChampions;
    private int magicDamageDealtToChampions;
    private int trueDamageDealtToChampions;
    private int physicalDamageTaken;
    private int magicDamageTaken;
    private int trueDamageTaken;
    @JsonAlias("goldEarned")
    private int golds;
    private String teamId;
    private boolean gameEndedInEarlySurrender;
    @JsonAlias("perks")
    private LOLRunesEntity runes;
    private LOLChampionNomenclature champion;
    private LOLItemNomenclature item0;
    private LOLItemNomenclature item1;
    private LOLItemNomenclature item2;
    private LOLItemNomenclature item3;
    private LOLItemNomenclature item4;
    private LOLItemNomenclature item5;
    private LOLItemNomenclature ward;
    private RIOTSummonerEntity summoner;
    private LOLSummonerSpellNomenclature summonerSpell1;
    private LOLSummonerSpellNomenclature summonerSpell2;
    @JsonAlias("summonerId")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String puuid;
    @JsonAlias("riotIdGameName")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String name;
    @JsonAlias("riotIdTagline")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String tag;
    @JsonAlias("summonerLevel")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int level;
    @JsonAlias("profileIcon")
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int icon;

    public void buildSummoner() {
        this.setSummoner(new RIOTSummonerEntity(id, puuid, name, tag, level, icon));
    }

    private void getSummonerSpell(String id, Consumer<LOLSummonerSpellNomenclature> setter) {
        LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLSummonerSpellNomenclatureRepository.class);
        setter.accept(summonerSpellNomenclatureRepository.findFirstById(id));
    }

    @JsonProperty("summoner1Id")
    private void unpackSummonerSpell1(String id) {
        this.getSummonerSpell(id, this::setSummonerSpell1);
    }

    @JsonProperty("summoner2Id")
    private void unpackSummonerSpell2(String id) {
        this.getSummonerSpell(id, this::setSummonerSpell2);
    }

    private void getItem(String id, Consumer<LOLItemNomenclature> setter) {
        LOLItemNomenclatureRepository itemNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLItemNomenclatureRepository.class);
        setter.accept(itemNomenclatureRepository.findFirstById(id));
    }

    @JsonProperty("item0")
    private void unpackItem0(String id) {
        this.getItem(id, this::setItem0);
    }

    @JsonProperty("item1")
    private void unpackItem1(String id) {
        this.getItem(id, this::setItem1);
    }

    @JsonProperty("item2")
    private void unpackItem2(String id) {
        this.getItem(id, this::setItem2);
    }

    @JsonProperty("item3")
    private void unpackItem3(String id) {
        this.getItem(id, this::setItem3);
    }

    @JsonProperty("item4")
    private void unpackItem4(String id) {
        this.getItem(id, this::setItem4);
    }

    @JsonProperty("item5")
    private void unpackItem5(String id) {
        this.getItem(id, this::setItem5);
    }

    @JsonProperty("item6")
    private void unpackWard(String id) {
        this.getItem(id, this::setWard);
    }

    @JsonProperty("championId")
    private void unpackChampion(String id) {
        LOLChampionNomenclatureRepository championNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLChampionNomenclatureRepository.class);
        this.setChampion(championNomenclatureRepository.findFirstById(id));
    }

    @JsonProperty("individualPosition")
    private void unpackRole(String role) {
        this.role = role;
    }
}
