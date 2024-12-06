package com.krazytop.nomenclature.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "TraitNomenclature")
public class TFTTraitNomenclature {

    @JsonAlias("apiName")
    private String id;
    @JsonAlias("desc")
    private String description;
    private String name;
    @JsonAlias("icon")
    private String image;
    private List<TFTEffectEntity> effects;

    @Data
    static class TFTEffectEntity {

        private Integer maxUnits;
        private Integer minUnits;
        private Integer style;
        private Map<String, Float> variables;
    }
}