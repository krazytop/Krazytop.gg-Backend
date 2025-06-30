package com.krazytop.repository.tft;

import com.krazytop.entity.tft.TFTMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TFTMatchRepository extends MongoRepository<TFTMatch, String> {

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}}", sort = "{'datetime': -1}")
    Page<TFTMatch> findAll(String puuid, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<TFTMatch> findAllByQueue(String puuid, List<String> queueIds, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?1}", sort = "{'datetime': -1}")
    Page<TFTMatch> findAllBySet(String puuid, int set, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?2, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<TFTMatch> findAllByQueueAndBySet(String puuid, List<String> queueIds, int set, PageRequest pageRequest);

    Optional<TFTMatch> findFirstById(String matchId);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}}", count = true)
    Integer countAll(String puuid);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue': {$in: ?1}}", count = true)
    Integer countAllByQueue(String puuid, List<String> queueIds);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?1}", count = true)
    Integer countAllBySet(String puuid, int set);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?2, 'queue':  {$in: ?1}}", count = true)
    Integer countAllByQueueAndBySet(String puuid, List<String> queueIds, int set);

}
