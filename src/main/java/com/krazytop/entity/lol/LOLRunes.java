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
public class LOLRunes {

    @JsonAlias("statPerks")
    private Map<String, Integer> stats;
    @JsonAlias("styles")
    private List<LOLRuneCategory> runeCategories;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LOLRuneCategory {

        private String style;
        private List<String> perks;

        @JsonProperty("selections")
        private void unpackStyles(List<JsonNode> nodes) {
            this.perks = nodes.stream().map(node -> node.get("perk").asText()).toList();
        }

    }

}
