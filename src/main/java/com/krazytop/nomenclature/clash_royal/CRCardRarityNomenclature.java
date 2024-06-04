package com.krazytop.nomenclature.clash_royal;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "CardRarityNomenclature")
public class CRCardRarityNomenclature {

    private String name;
    private int relativeLevel;
    private List<Integer> upgradeCost;

}
