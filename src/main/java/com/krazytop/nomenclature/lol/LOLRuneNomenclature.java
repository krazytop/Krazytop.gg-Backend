package com.krazytop.nomenclature.lol;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "RuneNomenclature")
public class LOLRuneNomenclature {

    private String id;
    private String name;
    private String image;

}
