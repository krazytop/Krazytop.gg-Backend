package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public abstract class LOLNomenclature {

    @JsonIgnore
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonIgnore
    private String image;
    @JsonAlias("blurb")
    @JsonProperty("description")
    private String description;

    @JsonProperty("image")
    private void unpackImage(JsonNode node) {
        this.setImage(node.get("full").asText());
    }
}
