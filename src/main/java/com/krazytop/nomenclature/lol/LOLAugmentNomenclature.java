package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(collection = "AugmentNomenclature")
public class LOLAugmentNomenclature extends LOLNomenclature {

    private Map<String, Integer> dataValues;

    @JsonProperty("iconLarge")
    private void unpackIconLarge(JsonNode node) {
        this.setImage(node.asText());
    }

}
