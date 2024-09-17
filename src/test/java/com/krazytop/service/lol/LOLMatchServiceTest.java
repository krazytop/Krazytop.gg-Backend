package com.krazytop.service.lol;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.krazytop.repository.lol.LOLMatchRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LOLMatchServiceTest {

    @InjectMocks
    private LOLMatchService matchService;

    @Mock
    private LOLMatchRepository matchRepository;

    @Test
    void testGetMatchesCount_AllQueues_AllRoles() {
        when(matchRepository.countByTeamsParticipantsSummonerPuuid(anyString())).thenReturn(1L);
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRole(anyString(), anyString())).thenReturn(2L);
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndQueueName(anyString(), anyString())).thenReturn(3L);
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueName(anyString(), anyString(), anyString())).thenReturn(4L);
        assertEquals(matchService.getMatchesCount("puuid", "ALL_QUEUES", "ALL_ROLES"), 1L);
        assertEquals(matchService.getMatchesCount("puuid", "ALL_QUEUES", "JUNGLE"), 2L);
        assertEquals(matchService.getMatchesCount("puuid", "ARAM", "ALL_ROLES"), 3L);
        assertEquals(matchService.getMatchesCount("puuid", "ARAM", "JUNGLE"), 4L);
    }
}