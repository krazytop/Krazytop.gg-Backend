package com.krazytop.repository.riot;

import com.krazytop.entity.riot.rank.RIOTRank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RIOTRankRepository extends MongoRepository<RIOTRank, String> {

    Optional<RIOTRank> findByPuuid(String puuid);
}
