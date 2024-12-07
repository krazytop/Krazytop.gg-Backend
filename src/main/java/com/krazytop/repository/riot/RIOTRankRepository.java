package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTRankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RIOTRankRepository extends MongoRepository<RIOTRankEntity, String> {

    RIOTRankEntity findFirstByPuuid(String puuid);
}
