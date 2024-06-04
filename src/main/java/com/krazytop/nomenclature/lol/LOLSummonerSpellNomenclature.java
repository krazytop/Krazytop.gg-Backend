package com.krazytop.nomenclature.lol;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "SummonerSpellNomenclature")
public class LOLSummonerSpellNomenclature {

    private String id;
    private String name;
    private String image;

}
