package com.krazytop.nomenclature.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "SummonerSpellNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLSummonerSpellNomenclature extends LOLNomenclature {

    private float cooldownBurn;
}
