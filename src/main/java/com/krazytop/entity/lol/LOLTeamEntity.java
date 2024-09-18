package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
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

    @JsonProperty("objectives")
    private LOLObjectivesEntity objectives;
    @JsonAlias("win")
    @JsonProperty("hasWin")
    private boolean hasWin;
    @JsonAlias("teamId")
    @JsonProperty("id")
    private String id;
    private List<LOLParticipantEntity> participants;
    private List<LOLChampionNomenclature> bans;

    @JsonProperty("bans")
    private void unpackBans(JsonNode node) {
        List<LOLChampionNomenclature> banArray = new ArrayList<>();
        LOLChampionNomenclatureRepository championNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLChampionNomenclatureRepository.class);
        node.forEach(ban -> banArray.add(championNomenclatureRepository.findFirstById(ban.get("championId").asText())));
        this.setBans(banArray);
    }

}
