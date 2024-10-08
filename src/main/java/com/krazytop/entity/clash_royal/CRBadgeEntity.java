package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRBadgeEntity {

    private String name;
    private int level;
    private int maxLevel;
    private int progress;
    private int target;
    private String image;

    @JsonProperty("iconUrls")
    private void unpackImage(JsonNode node) {
        this.image = node.get("large").asText();
    }

}
