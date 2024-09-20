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
    void testGetLocalMatches_OK() {
        when(matchService.getLocalMatches(anyString(), anyInt(), anyString(), anyString())).thenReturn(List.of(new LOLMatchEntity()));
        ResponseEntity<List<LOLMatchEntity>> response = matchController.getLocalMatches("puuid", 1, "queue", "role");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(matchService, times(1)).getLocalMatches(anyString(), anyInt(), anyString(), anyString());
    }

    @Test
    void testGetLocalMatches_ERROR() {
        when(matchService.getLocalMatches(anyString(), anyInt(), anyString(), anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<List<LOLMatchEntity>> response = matchController.getLocalMatches("puuid", 1, "queue", "role");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(matchService, times(1)).getLocalMatches(anyString(), anyInt(), anyString(), anyString());
    }

    @Test
    void testGetLocalMatchesCount_OK() {
        when(matchService.getLocalMatchesCount(anyString(), anyString(), anyString())).thenReturn(1L);
        ResponseEntity<Long> response = matchController.getLocalMatchesCount("puuid", "queue", "role");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(matchService, times(1)).getLocalMatchesCount(anyString(), anyString(), anyString());
    }

    @Test
    void testGetLocalMatchesCount_ERROR() {
        when(matchService.getLocalMatchesCount(anyString(), anyString(), anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<Long> response = matchController.getLocalMatchesCount("puuid", "queue", "role");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(matchService, times(1)).getLocalMatchesCount(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalMatches_OK() {
        ResponseEntity<String> response = matchController.updateRemoteToLocalMatches("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(matchService, times(1)).updateRemoteToLocalMatches(anyString());
    }

    @Test
    void testUpdateRemoteToLocalMatches_ERROR() {
        doThrow(RuntimeException.class).when(matchService).updateRemoteToLocalMatches(anyString());
        ResponseEntity<String> response = matchController.updateRemoteToLocalMatches("puuid");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(matchService, times(1)).updateRemoteToLocalMatches(anyString());
    }
}