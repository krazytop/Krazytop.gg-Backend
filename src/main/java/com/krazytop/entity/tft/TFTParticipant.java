package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.riot.RIOTSummoner;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTParticipant {

    private RIOTSummoner summoner = new RIOTSummoner();
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
    private List<TFTUnit> units;
    private List<TFTTrait> traits;
    private List<String> augments;
    @JsonAlias("win")
    private Boolean hasWin;

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
