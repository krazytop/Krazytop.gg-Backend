package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "PresentationNodeNomenclature")
public class DestinyPresentationNodeNomenclature {

    @Id private Long hash;
    private String name;
    private String description;
    private String icon;
    private Long nodeType;
    private boolean isSeasonal;
    private DestinyObjectiveNomenclature objective;
    private List<Long> childrenNodeHash;
    private List<DestinyCollectibleNomenclature> childrenCollectible;
    private List<DestinyRecordNomenclature> childrenRecord;
    private List<DestinyMetricNomenclature> childrenMetric;
    private List<DestinyItemNomenclature> childrenCraftable;
    private List<Long> parentNodeHashes;
}
