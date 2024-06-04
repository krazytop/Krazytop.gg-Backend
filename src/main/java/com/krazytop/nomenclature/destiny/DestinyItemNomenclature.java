package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ItemNomenclature")
public class DestinyItemNomenclature {

    @Id private Long hash;
    private String name;
    private String description;
    private String icon;
    private String itemTypeDisplayName;
    private Long maxStackSize;
    private Long bucketTypeHash;
    private Long recoveryBucketTypeHash;
    private Long tierTypeHash;
    private boolean isInstanceItem;
    private String tierTypeName;
    private Long equipmentSlotTypeHash;
    private Long summaryItemHash;
    private String iconWatermark;
    private boolean nonTransferrable;
    private Long specialItemType;
    private Long itemType;
    private Long itemSubType;
    private Long classType;
    private boolean equippable;
    private Long defaultDamageType;
}
