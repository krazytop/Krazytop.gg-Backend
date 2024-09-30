package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "RuneNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLRuneNomenclature extends LOLNomenclature {

    @JsonAlias("longDesc")
    @JsonProperty("longDescription")
    private String longDescription;

    @JsonProperty("id")
    private void unpackId(JsonNode node) {
        this.setId(node.asText());
    }

    @JsonProperty("icon")
    private void unpackIcon(JsonNode node) {
        this.setImage(node.asText());
    }

}
