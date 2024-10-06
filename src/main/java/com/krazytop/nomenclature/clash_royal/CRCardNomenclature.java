package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "CardNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRCardNomenclature {

    private int id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String description;
    private String type;
    private int elixir;
    private String rarity;
    private String image;

    @JsonProperty("key")
    private void unpackKey(JsonNode node) {
        this.image = String.format("%s.png", node.asText());
    }

    @JsonProperty("_lang")
    private void unpackLang(JsonNode node) {
        this.name = node.get("name").get("fr").asText();
        this.description = node.get("description").get("fr").asText();
    }
}
