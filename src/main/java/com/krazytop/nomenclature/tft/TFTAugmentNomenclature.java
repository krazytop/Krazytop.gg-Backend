package com.krazytop.nomenclature.tft;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "AugmentNomenclature")
public class TFTAugmentNomenclature {

    private String id;
    private String name;
    private String image;

}
