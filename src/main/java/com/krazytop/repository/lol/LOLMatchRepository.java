package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LOLMatchRepository extends MongoRepository<LOLMatchEntity, String> {

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0}}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAll(String puuid, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue._id':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAllByQueue(String puuid, List<String> queueIds, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?1}}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAllByRole(String puuid, String role, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?2}}, 'queue._id':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAllByQueueAndByRole(String puuid, List<String> queueIds, String role, PageRequest pageRequest);

    LOLMatchEntity findFirstById(String matchId);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0}}}", sort = "{'datetime': -1}", count = true)
    Long countAll(String puuid);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue._id': {$in: ?1}}", sort = "{'datetime': -1}", count = true)
    Long countAllByQueue(String puuid, List<String> queueIds);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?1}}}", count = true)
    Long countAllByRole(String puuid, String role);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?2}}, 'queue._id':  {$in: ?1}}", count = true)
    Long countAllByQueueAndByRole(String puuid, List<String> queueIds, String role);
}
