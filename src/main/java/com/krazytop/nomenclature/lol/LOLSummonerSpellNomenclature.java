package com.krazytop.nomenclature.lol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "SummonerSpellNomenclature")
public class LOLSummonerSpellNomenclature extends LOLNomenclature {

    private int cooldownBurn;
}
