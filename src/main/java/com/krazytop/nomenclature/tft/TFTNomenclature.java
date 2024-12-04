package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public abstract class TFTNomenclature {

    private String id;
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;
    //@JsonAlias({"blurb", "shortDesc", "desc"})
    //private String description;

    @JsonProperty("image")
    private String unpackImage(JsonNode node) {
        this.setImage(node.get("full").asText());
        return image;
    }
}
