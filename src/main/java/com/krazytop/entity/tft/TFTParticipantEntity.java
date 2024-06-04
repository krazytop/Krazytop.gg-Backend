package com.krazytop.entity.tft;

import com.krazytop.nomenclature.tft.TFTAugmentNomenclature;
import lombok.Data;

import java.util.List;

@Data
public class TFTParticipantEntity {

    private String puuid;
    private List<TFTAugmentNomenclature> augments;
    private List<String> augmentsIds;
    private int lastRound;
    private int level;
    private int placement;
    private double lifetime;
    private List<TFTUnitEntity> units;
    private List<TFTTraitEntity> traits;

}
