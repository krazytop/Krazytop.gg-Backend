package com.krazytop.service.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.krazytop.repository.lol.LOLMatchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LOLMatchServiceTest {

    @InjectMocks
    private LOLMatchService matchService;

    @Mock
    private LOLMatchRepository matchRepository;

    @Test
    void testGetMatchesCount() {
        when(matchRepository.countByTeamsParticipantsSummonerPuuid(anyString())).thenReturn(1L);
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRole(anyString(), anyString())).thenReturn(2L);
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndQueueName(anyString(), anyString())).thenReturn(3L);
        when(matchRepository.countByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueName(anyString(), anyString(), anyString())).thenReturn(4L);
        assertEquals(matchService.getMatchesCount("puuid", "ALL_QUEUES", "ALL_ROLES"), 1L);
        assertEquals(matchService.getMatchesCount("puuid", "ALL_QUEUES", "JUNGLE"), 2L);
        assertEquals(matchService.getMatchesCount("puuid", "ARAM", "ALL_ROLES"), 3L);
        assertEquals(matchService.getMatchesCount("puuid", "ARAM", "JUNGLE"), 4L);
    }

    @ParameterizedTest
    @MethodSource("testGetMatchesData")
    void testGetMatches(String queue, String role, List<LOLMatchEntity> expectedMatches) {
        Page<LOLMatchEntity> pageMatch1 = new PageImpl<>(List.of(new LOLMatchEntity("1")));
        Page<LOLMatchEntity> pageMatch2 = new PageImpl<>(List.of(new LOLMatchEntity("2")));
        Page<LOLMatchEntity> pageMatch3 = new PageImpl<>(List.of(new LOLMatchEntity("3")));
        Page<LOLMatchEntity> pageMatch4 = new PageImpl<>(List.of(new LOLMatchEntity("4")));

        ReflectionTestUtils.setField(matchService, "pageSize", 1);

        when(matchRepository.findByTeamsParticipantsSummonerPuuidOrderByDatetimeDesc(anyString(), any())).thenReturn(pageMatch1);
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleOrderByDatetimeDesc(anyString(), anyString(), any())).thenReturn(pageMatch2);
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), any())).thenReturn(pageMatch3);
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), anyString(), any())).thenReturn(pageMatch4);

        assertEquals(matchService.getMatches("puuid", 0, queue, role), expectedMatches);
    }

    static Stream<Arguments> testGetMatchesData() {
        return Stream.of(
                Arguments.of("ALL_QUEUES", "ALL_ROLES", List.of(new LOLMatchEntity("1"))),
                Arguments.of("ALL_QUEUES", "JUNGLE", List.of(new LOLMatchEntity("2"))),
                Arguments.of("ARAM", "ALL_ROLES", List.of(new LOLMatchEntity("3"))),
                Arguments.of("ARAM", "JUNGLE", List.of(new LOLMatchEntity("4")))
        );
    }

    @Test
    void testGetMatches() {
        ReflectionTestUtils.setField(matchService, "pageSize", 1);
        LOLMatchEntity match1 = new LOLMatchEntity();
        match1.setId("1");
        Page<LOLMatchEntity> pageMatch1 = new PageImpl<>(List.of(match1));
        LOLMatchEntity match2 = new LOLMatchEntity();
        match2.setId("2");
        Page<LOLMatchEntity> pageMatch2 = new PageImpl<>(List.of(match2));
        LOLMatchEntity match3 = new LOLMatchEntity();
        match3.setId("3");
        Page<LOLMatchEntity> pageMatch3 = new PageImpl<>(List.of(match3));
        LOLMatchEntity match4 = new LOLMatchEntity();
        match4.setId("4");
        Page<LOLMatchEntity> pageMatch4 = new PageImpl<>(List.of(match4));
        when(matchRepository.findByTeamsParticipantsSummonerPuuidOrderByDatetimeDesc(anyString(), any())).thenReturn(pageMatch1);
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleOrderByDatetimeDesc(anyString(), anyString(), any())).thenReturn(pageMatch2);
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), any())).thenReturn(pageMatch3);
        when(matchRepository.findByTeamsParticipantsSummonerPuuidAndTeamsParticipantsRoleAndQueueNameOrderByDatetimeDesc(anyString(), anyString(), anyString(), any())).thenReturn(pageMatch4);
        assertEquals(matchService.getMatches("puuid", 0, "ALL_QUEUES", "ALL_ROLES"), List.of(match1));
        assertEquals(matchService.getMatches("puuid", 0, "ALL_QUEUES", "JUNGLE"), List.of(match2));
        assertEquals(matchService.getMatches("puuid", 0, "ARAM", "ALL_ROLES"), List.of(match3));
        assertEquals(matchService.getMatches("puuid", 0, "ARAM", "JUNGLE"), List.of(match4));
    }
}