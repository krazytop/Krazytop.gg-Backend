package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "AccountLevelNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRAccountLevelNomenclature {

    @Id
    @JsonAlias("name")
    private int level;
    @JsonAlias("exp_to_next_level")
    private int expToNextLevel;
    @JsonAlias("tower_level")
    private int towerLevel;
    @JsonAlias("summoner_level")
    private int summonerLevel;
}
