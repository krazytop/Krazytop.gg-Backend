package com.krazytop.nomenclature.clash_royal;

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

    @Id
    @JsonProperty("name")
    private String name;
    @JsonProperty("relative_level")
    private int relativeLevel;
    @JsonProperty("upgrade_material_count")
    private List<Integer> upgradeCost;

}
