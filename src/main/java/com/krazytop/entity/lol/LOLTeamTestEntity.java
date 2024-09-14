package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class LOLTeamTestEntity {

    @JsonIgnore
    private List<LOLChampionNomenclature> bans;
    private LOLObjectivesEntity objectives;
    @JsonProperty("win")
    private boolean hasWin;
    @JsonProperty("teamId")
    private String id;
    @JsonIgnore
    private List<LOLParticipantEntity> participants = new ArrayList<>();

    @JsonProperty("objectives")
    private void unpackObjectives(JsonNode map) {
        this.setObjectives(new LOLObjectivesEntity(
                map.get("baron").get("kills").asInt(),
                map.get("champion").get("kills").asInt(),
                map.get("dragon").get("kills").asInt(),
                map.get("horde").get("kills").asInt(),
                map.get("inhibitor").get("kills").asInt(),
                map.get("riftHerald").get("kills").asInt(),
                map.get("tower").get("kills").asInt()
        ));
    }
}
