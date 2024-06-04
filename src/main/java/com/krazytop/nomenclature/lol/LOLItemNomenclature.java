package com.krazytop.nomenclature.lol;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ItemNomenclature")
public class LOLItemNomenclature {

    private String id;
    private String name;
    private String image;

}
