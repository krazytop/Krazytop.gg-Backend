package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLSummonerSpellNomenclature extends LOLNomenclature {

    private String tooltip;
    private Float cooldownBurn;

    @JsonProperty("key")
    private void unpackKey(JsonNode node) {
        this.setId(node.asText());
    }
}
