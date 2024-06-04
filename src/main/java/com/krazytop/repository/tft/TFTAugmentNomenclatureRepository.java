package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTAugmentNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTAugmentNomenclatureRepository extends MongoRepository<TFTAugmentNomenclature, String> {

    TFTAugmentNomenclature findFirstById(String augmentId);
}
