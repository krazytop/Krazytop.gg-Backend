package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.service.lol.LOLMatchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        ResponseEntity<List<LOLMatchEntity>> response = matchController.getLocalMatches("puuid", 1, "queue", "role");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        verify(matchService, times(1)).getLocalMatches(anyString(), anyInt(), anyString(), anyString());
    }

    @Test
    void testGetLocalMatchesCount() {
        when(matchService.getLocalMatchesCount(anyString(), anyString(), anyString())).thenReturn(1L);
        ResponseEntity<Long> response = matchController.getLocalMatchesCount("puuid", "queue", "role");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(matchService, times(1)).getLocalMatchesCount(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalMatches() {
        doNothing().when(matchService).updateRemoteToLocalMatches(anyString());
        ResponseEntity<Boolean> response = matchController.updateRemoteToLocalMatches("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
        verify(matchService, times(1)).updateRemoteToLocalMatches(anyString());
    }
}