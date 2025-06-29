package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTSummoner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RIOTSummonerRepository extends MongoRepository<RIOTSummoner, String> {

    @Query("{'region' : ?0, 'tag' : {$regex : '^?1$', $options : 'i'}, 'name' : {$regex : '^?2$', $options : 'i'}}")
    Optional<RIOTSummoner> findFirstByRegionAndTagAndName(String region, String tag, String name);

    Optional<RIOTSummoner> findFirstByPuuid(String puuid);
}
