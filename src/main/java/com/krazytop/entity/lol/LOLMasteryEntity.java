package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import com.krazytop.repository.lol.LOLChampionNomenclatureRepository;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Mastery")
public class LOLMasteryEntity {

    private String puuid;
    @JsonAlias("championLevel")
    private int level;
    @JsonAlias("championPoints")
    private int points;
    private LOLChampionNomenclature champion;

    @JsonProperty("championId")
    private void unpackChampion(String id) {
        LOLChampionNomenclatureRepository championNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLChampionNomenclatureRepository.class);
        this.setChampion(championNomenclatureRepository.findFirstById(id));
    }
}

