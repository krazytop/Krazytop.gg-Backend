package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LOLTeamTestEntity {

    @JsonIgnore
    private List<LOLChampionNomenclature> bannedChampions;
    @JsonIgnore
    private LOLObjectivesEntity objectives;
    @JsonProperty("win")
    private boolean hasWin;
    @JsonProperty("teamId")
    private String id;
    @JsonIgnore
    private List<LOLParticipantEntity> participants = new ArrayList<>();
}
