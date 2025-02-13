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

import java.io.IOException;
import java.net.URISyntaxException;

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
    void testGetLocalSummoner_OK() {
        when(summonerService.getLocalSummoner(anyString(), anyString(), anyString())).thenReturn(new RIOTSummonerEntity());
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).getLocalSummoner(anyString(), anyString(), anyString());
    }

    @Test
    void testGetLocalSummoner_NO_CONTENT() {
        when(summonerService.getLocalSummoner(anyString(), anyString(), anyString())).thenReturn(null);
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(summonerService, times(1)).getLocalSummoner(anyString(), anyString(), anyString());
    }

    @Test
    void testGetLocalSummoner_ERROR() {
        when(summonerService.getLocalSummoner(anyString(), anyString(), anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(summonerService, times(1)).getLocalSummoner(anyString(), anyString(), anyString());
    }

    @Test
    void testGetRemoteSummoner_OK() throws URISyntaxException, IOException {
        when(summonerService.getRemoteSummonerByNameAndTag(anyString(), anyString(), anyString())).thenReturn(new RIOTSummonerEntity());
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getRemoteSummoner("region", "tag", "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).getRemoteSummonerByNameAndTag(anyString(), anyString(), anyString());
    }

    @Test
    void testGetRemoteSummoner_NO_CONTENT() throws URISyntaxException, IOException {
        when(summonerService.getRemoteSummonerByNameAndTag(anyString(), anyString(), anyString())).thenReturn(null);
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getRemoteSummoner("region", "tag", "name");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(summonerService, times(1)).getRemoteSummonerByNameAndTag(anyString(), anyString(), anyString());
    }

    @Test
    void testGetRemoteSummoner_ERROR() throws URISyntaxException, IOException {
        when(summonerService.getRemoteSummonerByNameAndTag(anyString(), anyString(), anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<RIOTSummonerEntity> response = summonerController.getRemoteSummoner("region", "tag", "name");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(summonerService, times(1)).getRemoteSummonerByNameAndTag(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalSummoner_OK() throws URISyntaxException, IOException {
        ResponseEntity<String> response = summonerController.updateRemoteToLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).updateRemoteToLocalSummoner(anyString(), anyString(), anyString());
    }

    @Test
    void testUpdateRemoteToLocalSummoner_ERROR() throws URISyntaxException, IOException {
        doThrow(RuntimeException.class).when(summonerService).updateRemoteToLocalSummoner(anyString(), anyString(),anyString());
        ResponseEntity<String> response = summonerController.updateRemoteToLocalSummoner("region", "tag", "name");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(summonerService, times(1)).updateRemoteToLocalSummoner(anyString(), anyString(), anyString());
    }
}