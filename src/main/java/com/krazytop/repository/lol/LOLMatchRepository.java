package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LOLMatchRepository extends MongoRepository<LOLMatchEntity, String> {

    Page<LOLMatchEntity> findByTeamsParticipantsSummonerPuuidOrderByDatetimeDesc(String puuid, PageRequest pageRequest);

    Page<LOLMatchEntity> findByTeamsParticipantsSummonerPuuidAndQueueNameOrderByDatetimeDesc(String puuid, String queueName, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?1}}}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleOrderByDatetimeDesc(String puuid, String role, PageRequest pageRequest);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?2}}, 'queue.name':  ?1}", sort = "{'datetime': -1}")
    Page<LOLMatchEntity> findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueNameOrderByDatetimeDesc(String puuid, String queueName, String role, PageRequest pageRequest);

    LOLMatchEntity findFirstById(String matchId);

    Long countByTeamsParticipantsSummonerPuuid(String puuid);

    Long countByTeamsParticipantsSummonerPuuidAndQueueName(String puuid, String queueName);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?1}}}", count = true)
    Long countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRole(String puuid, String role);

    @Query(value = "{'teams.participants': {$elemMatch: {'summoner.puuid': ?0, 'role': ?2}}, 'queue.name':  ?1}", count = true)
    Long countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueName(String puuid, String queueName, String role);
}
