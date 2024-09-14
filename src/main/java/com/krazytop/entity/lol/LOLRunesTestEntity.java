package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLRuneNomenclature;
import com.krazytop.repository.lol.LOLRuneNomenclatureRepository;
import lombok.Data;

import java.util.function.Consumer;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLRunesTestEntity {

    private LOLRuneNomenclature primaryRuneCategory;
    private LOLRuneNomenclature primaryRuneFirstPerk;
    private LOLRuneNomenclature primaryRuneSecondPerk;
    private LOLRuneNomenclature primaryRuneThirdPerk;
    private LOLRuneNomenclature primaryRuneFourthPerk;

    private LOLRuneNomenclature secondaryRuneCategory;
    private LOLRuneNomenclature secondaryRuneFirstPerk;
    private LOLRuneNomenclature secondaryRuneSecondaryPerk;

    private void getRune(String id, Consumer<LOLRuneNomenclature> setter) {
        LOLRuneNomenclatureRepository runeNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLRuneNomenclatureRepository.class);
        setter.accept(runeNomenclatureRepository.findFirstById(id));
    }

    @JsonProperty("styles")
    private void unpackStyles(JsonNode node) {
        this.unpackPrimaryRunes(node.get(0));
        this.unpackSecondaryRunes(node.get(1));
    }

    private void unpackPrimaryRunes(JsonNode node) {
        this.getRune(node.get("style").asText(), this::setPrimaryRuneCategory);
        JsonNode selections = node.get("selections");
        this.getRune(selections.get(0).get("perk").asText(), this::setPrimaryRuneFirstPerk);
        this.getRune(selections.get(1).get("perk").asText(), this::setPrimaryRuneSecondPerk);
        this.getRune(selections.get(2).get("perk").asText(), this::setPrimaryRuneThirdPerk);
        this.getRune(selections.get(3).get("perk").asText(), this::setPrimaryRuneFourthPerk);
    }

    private void unpackSecondaryRunes(JsonNode node) {
        this.getRune(node.get("style").asText(), this::setSecondaryRuneCategory);
        JsonNode selections = node.get("selections");
        this.getRune(selections.get(0).get("perk").asText(), this::setSecondaryRuneFirstPerk);
        this.getRune(selections.get(1).get("perk").asText(), this::setSecondaryRuneSecondaryPerk);
    }

}
