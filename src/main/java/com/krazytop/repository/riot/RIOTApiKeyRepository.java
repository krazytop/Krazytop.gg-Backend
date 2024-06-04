package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTApiKeyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RIOTApiKeyRepository extends MongoRepository<RIOTApiKeyEntity, String> {

    RIOTApiKeyEntity findFirstByOrderByKeyAsc();

}
