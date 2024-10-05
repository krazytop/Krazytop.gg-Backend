package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRBadgeEntity {

    @JsonProperty("name")
    private String name;
    @JsonProperty("level")
    private int level;
    @JsonProperty("maxLevel")
    private int maxLevel;
    @JsonProperty("progress")
    private int progress;
    @JsonProperty("target")
    private int target;
    @JsonIgnore
    private String image;

    @JsonProperty("iconUrls")
    private void unpackImage(JsonNode node) {
        this.image = node.get("large").asText();
    }

}
