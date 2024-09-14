package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import com.krazytop.repository.lol.LOLChampionNomenclatureRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LOLTeamEntity {

    private List<LOLChampionNomenclature> bans;
    @JsonProperty("objectives")
    private LOLObjectivesEntity objectives;
    @JsonProperty("win")
    private boolean hasWin;
    @JsonProperty("teamId")
    private String id;
    private List<LOLParticipantEntity> participants;

    @JsonProperty("bans")
    private void unpackBans(JsonNode node) {
        List<LOLChampionNomenclature> banArray = new ArrayList<>();
        LOLChampionNomenclatureRepository championNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLChampionNomenclatureRepository.class);
        node.forEach(ban -> banArray.add(championNomenclatureRepository.findFirstById(ban.get("championId").asText())));
        this.setBans(banArray);
    }

}
