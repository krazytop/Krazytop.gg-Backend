package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CRLeaguesEntity {

    @JsonProperty("currentSeason")
    private CRLeagueEntity currentSeason;
    @JsonProperty("previousSeason")
    private CRLeagueEntity previousSeason;
    @JsonProperty("bestSeason")
    private CRLeagueEntity bestSeason;

}
