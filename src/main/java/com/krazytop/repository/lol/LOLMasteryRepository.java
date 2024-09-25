package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMasteryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LOLMasteryRepository extends MongoRepository<LOLMasteryEntity, String> {

    List<LOLMasteryEntity> findAllByPuuid(String puuid);
    void deleteAllByPuuid(String puuid);
}
