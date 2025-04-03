package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface RIOTSummonerRepository extends MongoRepository<RIOTSummonerEntity, String> {

    @Query("{'tag' : {$regex : '^?0$', $options : 'i'}, 'name' : {$regex : '^?1$', $options : 'i'}}")
    Optional<RIOTSummonerEntity> findFirstByTagAndName(String tag, String name);

    Optional<RIOTSummonerEntity> findFirstById(String summonerId);
}
