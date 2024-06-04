package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTQueueNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTQueueNomenclatureRepository extends MongoRepository<TFTQueueNomenclature, String> {

    TFTQueueNomenclature findFirstById(String queueId);
}
