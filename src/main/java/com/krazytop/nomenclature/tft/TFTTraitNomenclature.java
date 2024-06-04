package com.krazytop.nomenclature.tft;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "TraitNomenclature")
public class TFTTraitNomenclature {

    private String id;
    private String image;
    private String name;

}
