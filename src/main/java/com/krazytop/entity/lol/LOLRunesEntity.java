package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLRunesEntity {

    @JsonAlias("statPerks")
    private Map<String, Integer> stats;
    @JsonAlias("styles")
    private List<LOLRuneCategoryEntity> runeCategories;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class LOLRuneCategoryEntity {

        private Integer style;
        private List<Integer> perks;

        @JsonProperty("selections")
        private void unpackStyles(List<JsonNode> nodes) {
            this.perks = nodes.stream().map(node -> node.get("perk").asInt()).toList();
        }

    }


}
