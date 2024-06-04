package com.krazytop.entity.lol;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_response.lol.LOLParticipantHTTPResponse;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import com.krazytop.nomenclature.lol.LOLItemNomenclature;
import com.krazytop.nomenclature.lol.LOLSummonerSpellNomenclature;
import lombok.Data;

@Data
public class LOLParticipantEntity {

    private int champLevel;
    private String championId;
    private LOLChampionNomenclature champion;
    private int kills;
    private int assists;
    private int deaths;
    private String role;
    private String item0Id;
    private LOLItemNomenclature item0;
    private String item1Id;
    private LOLItemNomenclature item1;
    private String item2Id;
    private LOLItemNomenclature item2;
    private String item3Id;
    private LOLItemNomenclature item3;
    private String item4Id;
    private LOLItemNomenclature item4;
    private String item5Id;
    private LOLItemNomenclature item5;
    private String wardId;
    private LOLItemNomenclature ward;
    private String puuid;
    private String summonerId;
    private String riotIdGameName;
    private String riotIdTagline;
    private String summonerLevel;
    private String profileIcon;
    private RIOTSummonerEntity summoner;
    private boolean gameEndedInEarlySurrender;
    private String teamId;
    private int visionScore;
    private int minions;
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
    private String summonerSpellId1;
    private String summonerSpellId2;
    private LOLSummonerSpellNomenclature summonerSpell1;
    private LOLSummonerSpellNomenclature summonerSpell2;
    private LOLRunesEntity runes;
    private int golds;
    private LOLParticipantHTTPResponse.Perks perks;
}
