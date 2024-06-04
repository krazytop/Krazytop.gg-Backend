package com.krazytop.nomenclature.destiny;

import com.krazytop.entity.destiny.DestinyIntervalObjectiveEntity;
import com.krazytop.entity.destiny.DestinyItemQuantityEntity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "RecordNomenclature")
public class DestinyRecordNomenclature {

    @Id private Long hash;
    private String name;
    private String description;
    private String icon;
    private List<DestinyObjectiveNomenclature> objectives;
    private List<DestinyIntervalObjectiveEntity> intervalObjectives;
    private String recordTypeName;
    private boolean hasExpiration;
    private String expirationDescription;
    private List<DestinyItemQuantityEntity> rewardItems;
    private Map<Long, String> titlesByGender;
}
