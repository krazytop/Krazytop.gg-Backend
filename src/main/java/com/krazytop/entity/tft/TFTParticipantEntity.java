package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import lombok.Data;

import java.util.ArrayList;
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
    @JsonAlias("total_damage_to_players")
    private Integer damageToPlayers;
    @JsonAlias("partner_group_id")
    private Integer teamId;
    @JsonAlias("win")
    private Boolean hasWin;
    private List<TFTUnitEntity> units;
    private List<TFTTraitEntity> traits;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<TFTItemNomenclature> augments;

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

    @JsonProperty("augments")
    private void unpackAugments(List<String> augmentIds) {
    }

}
