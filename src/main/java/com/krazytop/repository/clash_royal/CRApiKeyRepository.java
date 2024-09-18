package com.krazytop.repository.clash_royal;

import com.krazytop.entity.clash_royal.CRApiKeyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRApiKeyRepository extends MongoRepository<CRApiKeyEntity, String> {

    CRApiKeyEntity findFirstByOrderByKeyAsc();
}
