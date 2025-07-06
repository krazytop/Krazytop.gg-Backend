package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTTrait {

    @JsonAlias("name")
    private String id;
    @JsonAlias("tier_current")
    private Integer tier;
    @JsonAlias("tier_total")
    private Integer maxTier;
    @JsonAlias("num_units")
    private Integer unitsNb;
    private Integer style;
}
