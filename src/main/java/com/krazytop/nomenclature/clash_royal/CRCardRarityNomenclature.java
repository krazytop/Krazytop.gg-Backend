package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "CardRarityNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRCardRarityNomenclature {

    @Id
    private String name;
    @JsonAlias("relative_level")
    private int relativeLevel;
    @JsonAlias("upgrade_material_count")
    private List<Integer> upgradeCost;

}
