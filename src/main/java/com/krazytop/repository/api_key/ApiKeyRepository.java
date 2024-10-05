package com.krazytop.repository.api_key;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.nomenclature.GameEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiKeyRepository extends MongoRepository<ApiKeyEntity, String> {

    ApiKeyEntity findFirstByGame(GameEnum game);
}
