package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonCreator;
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

import java.util.function.Consumer;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLParticipantEntity {

    @JsonProperty("champLevel")
    private int champLevel;
    @JsonProperty("kills")
    private int kills;
    @JsonProperty("assists")
    private int assists;
    @JsonProperty("deaths")
    private int deaths;
    @JsonProperty("individualPosition")
    private String role;
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
    @JsonProperty("goldEarned")
    private int golds;
    @JsonProperty("teamId")
    private String teamId;
    @JsonProperty("gameEndedInEarlySurrender")
    private boolean gameEndedInEarlySurrender;
    @JsonProperty("perks")
    private LOLRunesEntity runes;
    private LOLChampionNomenclature champion;
    private LOLItemNomenclature item0;
    private LOLItemNomenclature item1;
    private LOLItemNomenclature item2;
    private LOLItemNomenclature item3;
    private LOLItemNomenclature item4;
    private LOLItemNomenclature item5;
    private LOLItemNomenclature ward;
    private RIOTSummonerEntity summoner; //TODO
    private LOLSummonerSpellNomenclature summonerSpell1;
    private LOLSummonerSpellNomenclature summonerSpell2;

    @JsonCreator
    private void createSummoner(@JsonProperty("summonerId") String id,
                                @JsonProperty("puuid") String puuid,
                                @JsonProperty("riotIdGameName") String name,
                                @JsonProperty("riotIdTagline") String tag,
                                @JsonProperty("summonerLevel") int level,
                                @JsonProperty("profileIcon") int icon) {
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

}
