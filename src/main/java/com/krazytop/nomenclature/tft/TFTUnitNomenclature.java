package com.krazytop.nomenclature.tft;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "UnitNomenclature")
public class TFTUnitNomenclature {

    private String id;
    private String name;

}
