package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "AccountLevelNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRAccountLevelNomenclature {

    @Id
    @JsonProperty("name")
    private int level;
    @JsonProperty("exp_to_next_level")
    private int expToNextLevel;
    @JsonProperty("tower_level")
    private int towerLevel;
    @JsonProperty("summoner_level")
    private int summonerLevel;
}
