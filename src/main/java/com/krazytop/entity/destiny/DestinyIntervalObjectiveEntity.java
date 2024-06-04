package com.krazytop.entity.destiny;

import com.krazytop.nomenclature.destiny.DestinyObjectiveNomenclature;
import lombok.Data;

import java.util.List;

@Data
public class DestinyIntervalObjectiveEntity {

    private Long score;
    private DestinyObjectiveNomenclature objective;
    private List<DestinyItemQuantityEntity> rewardItems;
}
