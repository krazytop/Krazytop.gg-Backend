package com.krazytop.api.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.repository.lol.LOLMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LOLMatchApi {

    private final LOLMatchRepository lolMatchRepository;
    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;

    @Autowired
    public LOLMatchApi(LOLMatchRepository lolMatchRepository) {
        this.lolMatchRepository = lolMatchRepository;
    }

    public List<LOLMatchEntity> getMatches(String puuid, int pageNb, String queue, String role) {
        PageRequest pageRequest = PageRequest.of(pageNb, pageSize);
        if (queue.equals("ALL_QUEUES")) {
            if (role.equals("ALL_ROLES")) {
                return lolMatchRepository.findByTeamsParticipantsSummonerPuuidOrderByDatetimeDesc(puuid, pageRequest).getContent();
            } else {
                return lolMatchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleOrderByDatetimeDesc(puuid, role, pageRequest).getContent();
            }
        } else {
            if (role.equals("ALL_ROLES")) {
                return lolMatchRepository.findByTeamsParticipantsSummonerPuuidAndQueueNameOrderByDatetimeDesc(puuid, queue, pageRequest).getContent();
            } else {
                return lolMatchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueNameOrderByDatetimeDesc(puuid, queue, role, pageRequest).getContent();
            }
        }
    }

    public void updateMatch(LOLMatchEntity match) {
        lolMatchRepository.save(match);
    }

    public long getMatchCount(String puuid, String queue, String role) {
        if (queue.equals("ALL_QUEUES")) {
            if (role.equals("ALL_ROLES")) {
                return lolMatchRepository.countByTeamsParticipantsSummonerPuuid(puuid);
            } else {
                return lolMatchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRole(puuid, role);
            }
        } else {
            if (role.equals("ALL_ROLES")) {
                return lolMatchRepository.countByTeamsParticipantsSummonerPuuidAndQueueName(puuid, queue);
            } else {
                return lolMatchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueName(puuid, queue, role);
            }
        }
    }

}
