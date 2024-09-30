package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "AccountLevelNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRAccountLevelNomenclature {

    @JsonAlias("name")
    @JsonProperty("level") @Id
    private int level;
    @JsonAlias("exp_to_next_level")
    @JsonProperty("expToNextLevel")
    private int expToNextLevel;
    @JsonAlias("tower_level")
    @JsonProperty("towerLevel")
    private int towerLevel;
    @JsonAlias("summoner_level")
    @JsonProperty("summonerLevel")
    private int summonerLevel;
}
