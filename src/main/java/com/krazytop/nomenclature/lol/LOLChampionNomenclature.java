package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ChampionNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLChampionNomenclature extends LOLNomenclature {

    @JsonProperty("title")
    private String title;
    @JsonProperty("stats")
    private Map<String, Integer> stats;
    @JsonProperty("tags")
    private List<String> tags;
}
