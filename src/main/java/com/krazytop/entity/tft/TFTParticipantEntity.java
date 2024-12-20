package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTParticipantEntity {

    private RIOTSummonerEntity summoner = new RIOTSummonerEntity();
    private Integer lastRound;
    private Integer level;
    @JsonAlias("gold_left")
    private Integer goldLeft;
    private Integer placement;
    @JsonAlias("players_eliminated")
    private Integer playersEliminated;
    @JsonAlias("time_eliminated")
    private Integer timeEliminated;
    @JsonAlias({"total_damage_to_players", "totalDamageToPlayers"})
    private Integer damageToPlayers;
    private List<TFTUnitEntity> units;
    private List<TFTTraitEntity> traits;
    private List<String> augments;

    @JsonProperty("puuid")
    private void unpackPuuid(String puuid) {
        this.getSummoner().setPuuid(puuid);
    }

    @JsonProperty("riotIdGameName")
    @JsonAlias("gameName")
    private void unpackName(String name) {
        this.getSummoner().setName(name);
    }

    @JsonProperty("riotIdTagline")
    @JsonAlias("tagLine")
    private void unpackTag(String tag) {
        this.getSummoner().setTag(tag);
    }

}
