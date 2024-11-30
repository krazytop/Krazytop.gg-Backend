package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public abstract class LOLNomenclature {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;
    @JsonAlias({"blurb", "shortDesc"})
    private String description;

    @JsonProperty("image")
    private String unpackImage(JsonNode node) {
        this.setImage(node.get("full").asText());
        return image;
    }
}
