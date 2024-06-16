package com.krazytop.repository.clash_royal;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRApiKeyRepository extends MongoRepository<String, String> {

    String findFirst();
}
