package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTUnitNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TFTUnitNomenclatureRepository extends MongoRepository<TFTUnitNomenclature, String> {

    @Query("{'_id' : {$regex : '^?0$', $options : 'i'}}")
    TFTUnitNomenclature findFirstById(String unitId);
}
