package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import com.krazytop.nomenclature.tft.TFTUnitNomenclature;
import com.krazytop.repository.tft.TFTItemNomenclatureRepository;
import com.krazytop.repository.tft.TFTUnitNomenclatureRepository;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTUnitEntity {

    private int rarity;
    private int tier;
    private TFTUnitNomenclature nomenclature;
    private List<TFTItemNomenclature> items = new ArrayList<>();

    @JsonProperty("character_id")
    private void unpackNomenclature(String id) {
        TFTUnitNomenclatureRepository unitNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(TFTUnitNomenclatureRepository.class);
        this.setNomenclature(unitNomenclatureRepository.findFirstById(id));
    }

    @JsonProperty("itemNames")
    private void unpackNomenclature(List<String> itemIds) {
        TFTItemNomenclatureRepository itemNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(TFTItemNomenclatureRepository.class);
        itemIds.forEach(itemId -> this.items.add(itemNomenclatureRepository.findFirstById(itemId)));
    }

}
