package com.krazytop.nomenclature.lol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "ItemNomenclature")
public class LOLItemNomenclature extends LOLNomenclature {

    private String plainText;
    private int baseGold;
    private int totalGold;
    private List<String> tags;
    private Map<String, Integer> stats;
    private List<String> toItems;
    private List<String> fromItems;
}
