package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import com.krazytop.nomenclature.tft.TFTUnitNomenclature;
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
    }

    @JsonProperty("itemNames")
    private void unpackNomenclature(List<String> itemIds) {
    }

}
