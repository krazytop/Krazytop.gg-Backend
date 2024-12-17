package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TFTItemNomenclatureRepository extends MongoRepository<TFTItemNomenclature, String> {

    @Query("{'_id' : {$regex : '^?0$', $options : 'i'}}")
    TFTItemNomenclature findFirstById(String itemId);
}
