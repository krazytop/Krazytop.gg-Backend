package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LOLMatchRepository extends MongoRepository<LOLMatch, String> {

    @Query(value = "{'owners': ?0}", sort = "{'datetime': -1}")
    Page<LOLMatch> findAll(String puuid, PageRequest pageRequest);

    @Query(value = "{'owners': ?0, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<LOLMatch> findAllByQueue(String puuid, List<String> queueIds, PageRequest pageRequest);

    @Query(value = "{'owners': ?0, 'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?1}}}", sort = "{'datetime': -1}")
    Page<LOLMatch> findAllByRole(String puuid, String role, PageRequest pageRequest);

    @Query(value = "{'owners': ?0, 'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?2}}, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<LOLMatch> findAllByQueueAndByRole(String puuid, List<String> queueIds, String role, PageRequest pageRequest);

    Optional<LOLMatch> findFirstById(String matchId);

    @Query(value = "{'owners': ?0}", count = true)
    Integer countAll(String puuid);

    @Query(value = "{'owners': ?0, 'queue': {$in: ?1}}", count = true)
    Integer countAllByQueue(String puuid, List<String> queueIds);

    @Query(value = "{'owners': ?0, 'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?1}}}", count = true)
    Integer countAllByRole(String puuid, String role);

    @Query(value = "{'owners': ?0, 'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?2}}, 'queue':  {$in: ?1}}", count = true)
    Integer countAllByQueueAndByRole(String puuid, List<String> queueIds, String role);
}
