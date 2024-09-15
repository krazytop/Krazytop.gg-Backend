package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLRankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLRankRepository extends MongoRepository<LOLRankEntity, String> {

    LOLRankEntity findFirstBySummonerIdAndQueueNameOrderByUpdateDateDesc(String summonerId, String queueName);

}
