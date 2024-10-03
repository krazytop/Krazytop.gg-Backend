package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMasteryEntity;
import com.krazytop.service.lol.LOLMasteryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLMasteryControllerTest {

    @InjectMocks
    private LOLMasteryController masteryController;

    @Mock
    private LOLMasteryService masteryService;

    @Test
    void testGetLocalMasteries_OK() {
        when(masteryService.getLocalMasteries(anyString())).thenReturn(List.of(new LOLMasteryEntity()));
        ResponseEntity<List<LOLMasteryEntity>> response = masteryController.getLocalMasteries("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(masteryService, times(1)).getLocalMasteries(anyString());
    }

    @Test
    void testGetLocalMasteries_ERROR() {
        when(masteryService.getLocalMasteries(anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<List<LOLMasteryEntity>> response = masteryController.getLocalMasteries("puuid");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(masteryService, times(1)).getLocalMasteries(anyString());
    }

    @Test
    void testUpdateRemoteToLocalMasteries_OK() throws URISyntaxException, IOException {
        ResponseEntity<String> response = masteryController.updateRemoteToLocalMasteries("puuid");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(masteryService, times(1)).updateRemoteToLocalMasteries(anyString());
    }

    @Test
    void testUpdateRemoteToLocalMasteriesERROR() throws URISyntaxException, IOException {
        doThrow(RuntimeException.class).when(masteryService).updateRemoteToLocalMasteries(anyString());
        ResponseEntity<String> response = masteryController.updateRemoteToLocalMasteries("puuid");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(masteryService, times(1)).updateRemoteToLocalMasteries(anyString());
    }
}