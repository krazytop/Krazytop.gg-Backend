package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTTraitNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTTraitNomenclatureRepository extends MongoRepository<TFTTraitNomenclature, String> {

    TFTTraitNomenclature findFirstById(String traitId);
}
