package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "MetricNomenclature")
public class DestinyMetricNomenclature {

    @Id private Long hash;
    private String description;
    private String name;
    private String icon;
    private DestinyObjectiveNomenclature trackingObjective;
    private Long nodeType;
    private List<Long> traitHashes;
}
