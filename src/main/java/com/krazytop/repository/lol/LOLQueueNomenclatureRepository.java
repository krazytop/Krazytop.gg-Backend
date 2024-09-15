package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLQueueNomenclatureRepository extends MongoRepository<LOLQueueNomenclature, String> {

    LOLQueueNomenclature findFirstById(String queueId);
    LOLQueueNomenclature findFirstByName(String queueName);
}
