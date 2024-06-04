package com.krazytop.nomenclature.destiny;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ObjectiveNomenclature")
public class DestinyObjectiveNomenclature {

    @Id private Long hash;
    private String name;
    private String description;
    private String progressDescription;
    private String icon;
    private Long completionValue;
    private Long scope;
    private Long locationHash; //TODO
    private boolean allowValueChangeWhenCompleted;
    private boolean isCountingDownward;
    private boolean allowNegativeValue;
    private boolean allowOvercompletion;
    private boolean showValueOnComplete;
    private boolean isDisplayOnlyObjective;
}
