package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRCardEntity {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("level")
    private int level;

    @JsonProperty("evolutionLevel")
    private int evolutionLevel;

    @JsonProperty("count")
    private int count;

    @JsonProperty("starLevel")
    private int starLevel;

    private int elixir;
    private String description;
    private String rarity;
    private String type;
    private int upgradeCost;
}
