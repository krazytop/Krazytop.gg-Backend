package com.krazytop.repository.tft;

import com.krazytop.entity.tft.TFTMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TFTMatchRepository extends MongoRepository<TFTMatchEntity, String> {

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}}", sort = "{'datetime': -1}")
    Page<TFTMatchEntity> findAll(String puuid, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<TFTMatchEntity> findAllByQueue(String puuid, List<String> queueIds, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?1}", sort = "{'datetime': -1}")
    Page<TFTMatchEntity> findAllBySet(String puuid, int set, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?2, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<TFTMatchEntity> findAllByQueueAndBySet(String puuid, List<String> queueIds, int set, PageRequest pageRequest);

    Optional<TFTMatchEntity> findFirstById(String matchId);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}}", count = true)
    Long countAll(String puuid);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue': {$in: ?1}}", count = true)
    Long countAllByQueue(String puuid, List<String> queueIds);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?1}", count = true)
    Long countAllBySet(String puuid, int set);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?2, 'queue':  {$in: ?1}}", count = true)
    Long countAllByQueueAndBySet(String puuid, List<String> queueIds, int set);

}
