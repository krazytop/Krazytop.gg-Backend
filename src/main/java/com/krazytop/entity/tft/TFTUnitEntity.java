package com.krazytop.entity.tft;

import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import lombok.Data;

import java.util.List;

@Data
public class TFTUnitEntity {

    private String id;
    private List<TFTItemNomenclature> items;
    private List<String> itemsIds;
    private int rarity;
    private int tier;
    private String name;

}
