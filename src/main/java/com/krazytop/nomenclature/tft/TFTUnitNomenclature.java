package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "UnitNomenclature")
public class TFTUnitNomenclature {

    @JsonAlias("apiName")
    private String id;
    private String name;
    @JsonAlias("tileIcon")
    private String image;
    private Integer cost;
    @JsonAlias("traits")
    private List<String> traits;
    private TFTAbilityEntity ability;

    @Data
    static class TFTAbilityEntity {

        private String name;
        @JsonAlias("icon")
        private String image;
        @JsonAlias("desc")
        private String description;
        private List<TFTVariableEntity> variables;

        @Data
        static class TFTVariableEntity {

            private String name;
            private List<Integer> value;
        }
    }
}