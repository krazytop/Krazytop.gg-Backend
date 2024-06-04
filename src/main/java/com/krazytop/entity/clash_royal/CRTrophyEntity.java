package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRTrophyEntity {

    @JsonAlias("id")
    private String date;

    @JsonProperty("trophies")
    private int trophies;

    @JsonProperty("bestTrophies")
    private int bestTrophies;

}
