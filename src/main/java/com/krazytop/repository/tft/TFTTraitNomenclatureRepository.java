package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTTraitNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TFTTraitNomenclatureRepository extends MongoRepository<TFTTraitNomenclature, String> {

    @Query("{'_id' : {$regex : '^?0$', $options : 'i'}}")
    TFTTraitNomenclature findFirstById(String traitId);
}
