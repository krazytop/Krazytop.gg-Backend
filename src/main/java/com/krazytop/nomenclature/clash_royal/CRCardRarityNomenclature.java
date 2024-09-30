package com.krazytop.nomenclature.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "CardRarityNomenclature")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRCardRarityNomenclature {

    @JsonProperty("name") @Id
    private String name;
    @JsonAlias("relative_level")
    @JsonProperty("relativeLevel")
    private int relativeLevel;
    @JsonAlias("upgrade_material_count")
    @JsonProperty("upgradeCost")
    private List<Integer> upgradeCost;

}
