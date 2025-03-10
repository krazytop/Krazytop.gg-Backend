package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLItemNomenclature extends LOLNomenclature {

    @JsonAlias("plaintext")
    private String plainText;
    private int baseGold;
    private int totalGold;
    private List<String> tags;
    private Map<String, Double> stats;
    @JsonAlias("into")
    private List<String> toItems;
    @JsonAlias("from")
    private List<String> fromItems;

    @JsonProperty("gold")
    private void unpackGold(JsonNode node) {
        this.setTotalGold(node.get("total").asInt());
        this.setBaseGold(node.get("base").asInt());
    }
}
