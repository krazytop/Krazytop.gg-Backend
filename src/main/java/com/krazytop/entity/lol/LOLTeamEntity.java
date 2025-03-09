package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLTeamEntity {

    private LOLObjectivesEntity objectives;
    @JsonAlias("win")
    private Boolean hasWin;
    @JsonAlias("teamId")
    private String id;
    private List<LOLParticipantEntity> participants;
    private List<String> bans;
    private Integer placement;

    @JsonProperty("bans")
    private void unpackBans(List<JsonNode> nodes) {
        this.bans = nodes.stream().map(node -> node.get("championId").asText()).toList();
    }

}
