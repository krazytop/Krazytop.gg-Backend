package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.service.lol.LOLRankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLRankControllerTest {

    @InjectMocks
    private LOLRankController rankController;

    @Mock
    private LOLRankService rankService;

    @Test
    void testGetLocalRank_OK() {
        when(rankService.getLocalRank(anyString(), anyString())).thenReturn(new LOLRankEntity());
        ResponseEntity<LOLRankEntity> response = rankController.getLocalRank("puuid", "queue");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString(), anyString());
    }

    @Test
    void testGetLocalRank_NO_CONTENT() {
        when(rankService.getLocalRank(anyString(), anyString())).thenReturn(null);
        ResponseEntity<LOLRankEntity> response = rankController.getLocalRank("puuid", "queue");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString(), anyString());
    }

    @Test
    void testGetLocalRank_ERROR() {
        when(rankService.getLocalRank(anyString(), anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<LOLRankEntity> response = rankController.getLocalRank("puuid", "queue");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalRank_OK() throws URISyntaxException, IOException {
        ResponseEntity<String> response = rankController.updateRemoteToLocalRank("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(rankService, times(1)).updateRemoteToLocalRank(anyString());
    }

    @Test
    void testUpdateRemoteToLocalRank_ERROR() throws URISyntaxException, IOException {
        doThrow(RuntimeException.class).when(rankService).updateRemoteToLocalRank(anyString());
        ResponseEntity<String> response = rankController.updateRemoteToLocalRank("puuid");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(rankService, times(1)).updateRemoteToLocalRank(anyString());
    }
}