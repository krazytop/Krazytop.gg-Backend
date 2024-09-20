package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.service.riot.RIOTSummonerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RIOTSummonerControllerTest {

    @InjectMocks
    private RIOTSummonerController summonerController;
    @Mock
    private RIOTSummonerService summonerService;

    @Test
    void tesGetLocalSummoner() {
        when(summonerService.getLocalSummoner(anyString(), anyString(), anyString())).thenReturn(new RIOTSummonerEntity());
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).getLocalSummoner(anyString(), anyString(), anyString());
    }

    @Test
    void testGetRemoteSummoner() {
        when(summonerService.getRemoteSummoner(anyString(), anyString(), anyString())).thenReturn(new RIOTSummonerEntity());
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getRemoteSummoner("region", "tag", "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).getRemoteSummoner(anyString(), anyString(), anyString());

    }

    @Test
    void testUpdateRemoteToLocalSummoner() {
        when(summonerService.updateRemoteToLocalSummoner(anyString(), anyString(), anyString())).thenReturn(new RIOTSummonerEntity());
        ResponseEntity<RIOTSummonerEntity> response = summonerController.updateRemoteToLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).updateRemoteToLocalSummoner(anyString(), anyString(), anyString());

    }
}