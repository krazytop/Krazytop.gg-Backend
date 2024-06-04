package com.krazytop.repository.clash_royal;

import com.krazytop.entity.clash_royal.CRCardEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRCardRepository extends MongoRepository<CRCardEntity, String> {

    CRCardEntity findFirstById(int id);
}
