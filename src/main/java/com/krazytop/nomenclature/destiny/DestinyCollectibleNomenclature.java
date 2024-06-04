package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "CollectibleNomenclature")
public class DestinyCollectibleNomenclature {

    @Id private Long hash;
    private String sourceString;
    private Long sourceHash;
    private DestinyItemNomenclature itemNomenclature;
    private Long nodeType;
}
