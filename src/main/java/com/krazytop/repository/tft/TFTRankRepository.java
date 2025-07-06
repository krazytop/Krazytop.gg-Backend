package com.krazytop.repository.tft;

import com.krazytop.entity.riot.rank.RIOTRank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TFTRankRepository extends MongoRepository<RIOTRank, String> {

    Optional<RIOTRank> findByPuuid(String puuid);

}
