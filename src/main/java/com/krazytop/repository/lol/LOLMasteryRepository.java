package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMasteriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LOLMasteryRepository extends MongoRepository<LOLMasteriesEntity, String> {

    Optional<LOLMasteriesEntity> findByPuuid(String puuid);
}
