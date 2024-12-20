package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RIOTSummonerRepository extends MongoRepository<RIOTSummonerEntity, String> {

    @Query("{'region' : ?0, 'tag' : {$regex : '^?1$', $options : 'i'}, 'name' : {$regex : '^?2$', $options : 'i'}}")
    RIOTSummonerEntity findFirstByRegionAndTagAndName(String region, String tag, String name);

    Optional<RIOTSummonerEntity> findFirstByPuuid(String puuid);
}
