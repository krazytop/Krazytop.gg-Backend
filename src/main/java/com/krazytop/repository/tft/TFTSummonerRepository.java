package com.krazytop.repository.tft;

import com.krazytop.entity.riot.RIOTSummoner;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TFTSummonerRepository extends MongoRepository<RIOTSummoner, String> {

    @Query("{'tag' : {$regex : '^?0$', $options : 'i'}, 'name' : {$regex : '^?1$', $options : 'i'}}")
    Optional<RIOTSummoner> findFirstByTagAndName(String tag, String name);

    Optional<RIOTSummoner> findFirstByPuuid(String puuid);

}
