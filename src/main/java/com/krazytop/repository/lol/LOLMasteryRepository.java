package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMasteries;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LOLMasteryRepository extends MongoRepository<LOLMasteries, String> {

    Optional<LOLMasteries> findByPuuid(String puuid);
}
