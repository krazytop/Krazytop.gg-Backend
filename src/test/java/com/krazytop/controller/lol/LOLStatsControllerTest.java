package com.krazytop.controller.lol;

import com.krazytop.service.lol.LOLStatsService;
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
class LOLStatsControllerTest {

    @InjectMocks
    private LOLStatsController statsController;
    @Mock
    private LOLStatsService statsService;

    @Test
    void testGetLatestMatchesResult_OK() {
        when(statsService.getLatestMatchesResult(anyString(), anyString(), anyString())).thenReturn(List.of("VICTORY"));
        ResponseEntity<List<String>> response = statsController.getLatestMatchesResult("puuid", "queue", "role");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(statsService, times(1)).getLatestMatchesResult(anyString(), anyString(), anyString());
    }

    @Test
    void testGetLatestMatchesResult_ERROR() {
        when(statsService.getLatestMatchesResult(anyString(), anyString(), anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<List<String>> response = statsController.getLatestMatchesResult("puuid", "queue", "role");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(statsService, times(1)).getLatestMatchesResult(anyString(), anyString(), anyString());
    }
}