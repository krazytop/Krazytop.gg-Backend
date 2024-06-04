package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRTrophiesEntity {

    @JsonProperty("currentSeason")
    private CRTrophyEntity currentSeason;

    @JsonProperty("previousSeason")
    private CRTrophyEntity previousSeason;

    @JsonProperty("bestSeason")
    private CRTrophyEntity bestSeason;

}
