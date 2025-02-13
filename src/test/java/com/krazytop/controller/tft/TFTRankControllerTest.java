package com.krazytop.controller.tft;

import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.service.tft.TFTRankService;
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
class TFTRankControllerTest {

    @InjectMocks
    private TFTRankController rankController;

    @Mock
    private TFTRankService rankService;

    @Test
    void testGetLocalRank_OK() {
        when(rankService.getLocalRank(anyString())).thenReturn(new RIOTRankEntity());
        ResponseEntity<RIOTRankEntity> response = rankController.getLocalRank("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString());
    }

    @Test
    void testGetLocalRank_NO_CONTENT() {
        when(rankService.getLocalRank(anyString())).thenReturn(null);
        ResponseEntity<RIOTRankEntity> response = rankController.getLocalRank("puuid");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString());
    }

    @Test
    void testGetLocalRank_ERROR() {
        when(rankService.getLocalRank(anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<RIOTRankEntity> response = rankController.getLocalRank("puuid");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(rankService, times(1)).getLocalRank(anyString());
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