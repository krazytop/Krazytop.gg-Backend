package com.krazytop.nomenclature.lol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ChampionNomenclature")
public class LOLChampionNomenclature extends LOLNomenclature {

    private String title;
    private Map<String, Integer> stats;
    private List<String> tags;
}
