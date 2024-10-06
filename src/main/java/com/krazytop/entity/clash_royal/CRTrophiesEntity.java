package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRTrophiesEntity {

    private CRTrophyEntity currentSeason;
    private CRTrophyEntity previousSeason;
    private CRTrophyEntity bestSeason;

}
