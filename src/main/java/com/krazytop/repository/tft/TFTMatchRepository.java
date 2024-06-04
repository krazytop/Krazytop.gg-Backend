package com.krazytop.repository.tft;

import com.krazytop.entity.tft.TFTMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTMatchRepository extends MongoRepository<TFTMatchEntity, String> {

    Page<TFTMatchEntity> findByParticipantsPuuidAndSetOrderByDatetimeDesc(String puuid, String set, PageRequest pageRequest);

    Page<TFTMatchEntity> findByParticipantsPuuidAndQueueQueueTypeAndSetOrderByDatetimeDesc(String puuid, String queueType, String set, PageRequest pageRequest);

    TFTMatchEntity findFirstById(String matchId);

    long countByParticipantsPuuidAndSet(String puuid, String set);

    long countByParticipantsPuuidAndQueueQueueTypeAndSet(String puuid, String queueType, String set);

}
