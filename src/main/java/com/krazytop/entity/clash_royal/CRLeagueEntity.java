package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRLeagueEntity {

    @JsonProperty("leagueNumber")
    private int leagueNumber;

    @JsonProperty("trophies")
    private int trophies;

    @JsonProperty("rank")
    private int rank;

}
