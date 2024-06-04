package com.krazytop.entity.destiny;

import com.krazytop.nomenclature.destiny.DestinyItemNomenclature;
import lombok.Data;

@Data
public class DestinyItemQuantityEntity {

    private Long quantity;
    private DestinyItemNomenclature item;
}
