package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.tft.TFTTraitNomenclature;
import com.krazytop.repository.tft.TFTTraitNomenclatureRepository;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTTraitEntity {

    @JsonAlias("tier_current")
    private int tier;
    private TFTTraitNomenclature nomenclature;

    @JsonProperty("name")
    private void unpackNomenclature(String id) {
        TFTTraitNomenclatureRepository traitNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(TFTTraitNomenclatureRepository.class);
        this.setNomenclature(traitNomenclatureRepository.findFirstById(id));
    }

}
