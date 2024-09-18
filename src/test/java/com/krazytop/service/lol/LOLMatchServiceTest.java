package com.krazytop.service.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.krazytop.repository.lol.LOLMatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLMatchServiceTest {

    @InjectMocks
    private LOLMatchService matchService;

    @Mock
    private LOLMatchRepository matchRepository;

    @Test
    void testGetMatchesCount_AllQueues_AllRoles() {
        when(matchRepository.countByTeamsParticipantsSummonerPuuid(anyString())).thenReturn(1L);
        assertEquals(1L, matchService.getLocalMatchesCount("puuid", "ALL_QUEUES", "ALL_ROLES"));
        verify(matchRepository, times(1)).countByTeamsParticipantsSummonerPuuid(anyString());
    }

    @Test
    void testGetMatchesCount_AllQueues_SpecificRole() {
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRole(anyString(), anyString())).thenReturn(2L);
        assertEquals(2L, matchService.getLocalMatchesCount("puuid", "ALL_QUEUES", "JUNGLE"));
        verify(matchRepository, times(1)).countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRole(anyString(), anyString());
    }

    @Test
    void testGetMatchesCount_SpecificQueue_AllRoles() {
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndQueueName(anyString(), anyString())).thenReturn(3L);
        assertEquals(3L, matchService.getLocalMatchesCount("puuid", "ARAM", "ALL_ROLES"));
        verify(matchRepository, times(1)).countByTeamsParticipantsSummonerPuuidAndQueueName(anyString(), anyString());
    }

    @Test
    void testGetMatchesCount_SpecificQueue_SpecificRole() {
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueName(anyString(), anyString(), anyString())).thenReturn(4L);
        assertEquals(4L, matchService.getLocalMatchesCount("puuid", "ARAM", "JUNGLE"));
        verify(matchRepository, times(1)).countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueName(anyString(), anyString(), anyString());
    }

    @Test
    void testGetMatches_AllQueues_AllRoles() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findByTeamsParticipantsSummonerPuuidOrderByDatetimeDesc(anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "ALL_QUEUES", "ALL_ROLES").isEmpty());
        verify(matchRepository, times(1)).findByTeamsParticipantsSummonerPuuidOrderByDatetimeDesc(anyString(), any());
    }

    @Test
    void testGetMatches_AllQueues_SpecificRole() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleOrderByDatetimeDesc(anyString(), anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "ALL_QUEUES", "JUNGLE").isEmpty());
        verify(matchRepository, times(1)).findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleOrderByDatetimeDesc(anyString(), anyString(), any());
    }

    @Test
    void testGetMatches_SpecificQueue_AllRoles() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "ARAM", "ALL_ROLES").isEmpty());
        verify(matchRepository, times(1)).findByTeamsParticipantsSummonerPuuidAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), any());
    }

    @Test
    void testGetMatches_SpecificQueue_SpecificRole() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        Page<LOLMatchEntity> page = new PageImpl<>(List.of(new LOLMatchEntity()));
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), anyString(), any())).thenReturn(page);
        assertFalse(matchService.getLocalMatches("puuid", 0, "ARAM", "JUNGLE").isEmpty());
        verify(matchRepository, times(1)).findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), anyString(), any());
    }

}