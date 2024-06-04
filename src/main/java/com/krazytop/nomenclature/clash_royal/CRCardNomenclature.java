package com.krazytop.nomenclature.clash_royal;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "CardNomenclature")
public class CRCardNomenclature {

    private int id;
    private String name;
    private String type;
    private int elixir;
    private String description;
    private String rarity;
}
