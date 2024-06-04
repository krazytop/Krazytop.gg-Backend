package com.krazytop.nomenclature.tft;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ItemNomenclature")
public class TFTItemNomenclature {

    private String id;
    private String name;
    private String image;

}
