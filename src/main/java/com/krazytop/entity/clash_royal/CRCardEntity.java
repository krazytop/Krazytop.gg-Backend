package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRCardNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardRarityNomenclatureRepository;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRCardEntity {

    private Integer level;
    private Integer evolutionLevel;
    private Integer count;
    private Integer starLevel;
    private Integer upgradeCost;
    private CRCardNomenclature nomenclature;

    @JsonProperty("id")
    private void unpackNomenclature(JsonNode node) {
        CRCardNomenclatureRepository cardNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(CRCardNomenclatureRepository.class);
        this.setNomenclature(cardNomenclatureRepository.findFirstById(node.asInt()));
    }

    @JsonProperty("rarity")
    private void unpackUpgradeCost(JsonNode node) {
        if (this.getLevel() != null) {
            CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(CRCardRarityNomenclatureRepository.class);
            String rarity = node.asText();
            rarity = rarity.substring(0, 1).toUpperCase() + rarity.substring(1);
            CRCardRarityNomenclature rarityNomenclature = cardRarityNomenclatureRepository.findFirstByName(rarity);
            this.setUpgradeCost(rarityNomenclature.getUpgradeCost().get(this.getLevel() - 1));
        }
    }
}
