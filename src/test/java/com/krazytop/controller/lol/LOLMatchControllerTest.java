package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.service.lol.LOLMatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLMatchControllerTest {

    @InjectMocks
    private LOLMatchController matchController;
    @Mock
    private LOLMatchService matchService;

    @Test
    void testGetLocalMatches() {
        when(matchService.getLocalMatches(anyString(), anyInt(), anyString(), anyString())).thenReturn(List.of(new LOLMatchEntity()));
        assertEquals(1, Objects.requireNonNull(matchController.getLocalMatches("puuid", 1, "queue", "role").getBody()).size());
        verify(matchService, times(1)).getLocalMatches(anyString(), anyInt(), anyString(), anyString());
    }

    @Test
    void testGetLocalMatchesCount() {
        when(matchService.getLocalMatchesCount(anyString(), anyString(), anyString())).thenReturn(1L);
        assertEquals(1, matchController.getLocalMatchesCount("puuid", "queue", "role").getBody());
        verify(matchService, times(1)).getLocalMatchesCount(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalMatches() {
        doNothing().when(matchService).updateRemoteToLocalMatches(anyString());
        assertEquals(Boolean.TRUE, matchController.updateRemoteToLocalMatches("puuid").getBody());
        verify(matchService, times(1)).updateRemoteToLocalMatches(anyString());
    }
}