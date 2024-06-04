package com.krazytop.repository.tft;

import com.krazytop.entity.tft.TFTRankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTRankRepository extends MongoRepository<TFTRankEntity, String> {

    TFTRankEntity findFirstBySummonerIdAndQueueTypeOrderByUpdateDateDesc(String summonerId, String queueType);

    TFTRankEntity findFirstBySummonerIdOrderByUpdateDateDesc(String summonerId);
}
