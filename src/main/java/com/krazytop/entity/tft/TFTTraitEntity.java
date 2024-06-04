package com.krazytop.entity.tft;

import lombok.Data;

@Data
public class TFTTraitEntity {

    private String id;
    private int numberUnits;
    private int style;
    private int currentTier;
    private int maxTier;
    private String image;
    private String name;

}
