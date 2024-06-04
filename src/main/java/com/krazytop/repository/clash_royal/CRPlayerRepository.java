package com.krazytop.repository.clash_royal;

import com.krazytop.entity.clash_royal.CRPlayerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRPlayerRepository extends MongoRepository<CRPlayerEntity, String> {

    CRPlayerEntity findFirstById(String playerId);
}
