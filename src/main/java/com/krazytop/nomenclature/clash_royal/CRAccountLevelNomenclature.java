package com.krazytop.nomenclature.clash_royal;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "AccountLevelNomenclature")
public class CRAccountLevelNomenclature {

    private int level;
    private int expToNextLevel;
    private int towerLevel;
}
