package com.krazytop.repository.riot;

import com.krazytop.entity.riot.rank.RIOTRankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RIOTRankRepository extends MongoRepository<RIOTRankEntity, String> {

    Optional<RIOTRankEntity> findByPuuid(String puuid);
}
