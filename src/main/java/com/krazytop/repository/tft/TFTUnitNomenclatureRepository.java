package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTUnitNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TFTUnitNomenclatureRepository extends MongoRepository<TFTUnitNomenclature, String> {

    TFTUnitNomenclature findFirstById(String unitId);
}
