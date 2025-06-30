package com.krazytop.repository.lol;

import com.krazytop.entity.riot.rank.RIOTRank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LOLRankRepository extends MongoRepository<RIOTRank, String> {

    Optional<RIOTRank> findByPuuid(String puuid);

}
