package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LOLMatchRepository extends MongoRepository<LOLMatchEntity, String> {

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0}}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAll(String summonerId, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0}}, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAllByQueue(String summonerId, List<String> queueIds, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0, 'role': ?1}}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAllByRole(String summonerId, String role, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0, 'role': ?2}}, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findAllByQueueAndByRole(String summonerId, List<String> queueIds, String role, PageRequest pageRequest);

    Optional<LOLMatchEntity> findFirstById(String matchId);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0}}}", count = true)
    Long countAll(String summonerId);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0}}, 'queue': {$in: ?1}}", count = true)
    Long countAllByQueue(String summonerId, List<String> queueIds);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0, 'role': ?1}}}", count = true)
    Long countAllByRole(String summonerId, String role);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.id': ?0, 'role': ?2}}, 'queue':  {$in: ?1}}", count = true)
    Long countAllByQueueAndByRole(String summonerId, List<String> queueIds, String role);
}
