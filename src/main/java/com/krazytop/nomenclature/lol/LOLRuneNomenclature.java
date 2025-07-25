package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLRuneNomenclature extends LOLNomenclature {

    private List<List<LOLRunePerkNomenclature>> perks = new ArrayList<>();

    @JsonProperty("slots")
    private void unpackPerks(List<JsonNode> nodes) {
        nodes.forEach(node -> this.perks.add(new ObjectMapper().convertValue(node.get("runes"), new TypeReference<>() {})));
    }
}
