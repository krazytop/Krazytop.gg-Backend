package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTItemNomenclatureRepository extends MongoRepository<TFTItemNomenclature, String> {

    TFTItemNomenclature findFirstById(String itemId);
}
