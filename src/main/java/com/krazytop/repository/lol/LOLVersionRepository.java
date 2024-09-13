package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLVersionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLVersionRepository extends MongoRepository<LOLVersionEntity, String> {

    LOLVersionEntity findFirstById(String id);

}
