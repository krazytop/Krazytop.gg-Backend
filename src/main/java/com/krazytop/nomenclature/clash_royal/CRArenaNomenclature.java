package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ArenaNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRArenaNomenclature {

    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String name;
    @JsonIgnore
    private String image;

    @JsonProperty("key")
    private void unpackKey(JsonNode node) {
        this.image = String.format("%s.png", node.asText());
    }
}
