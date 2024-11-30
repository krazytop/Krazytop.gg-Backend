package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "TraitNomenclature")
public class TFTTraitNomenclature {

    private String id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String image;
    private String name;

    @JsonProperty("image")
    private String unpackImage(JsonNode node) {
        this.setImage(node.get("full").asText());
        return image;
    }
}
