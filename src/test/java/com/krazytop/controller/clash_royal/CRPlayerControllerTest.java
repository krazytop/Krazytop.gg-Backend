package com.krazytop.controller.clash_royal;

import com.krazytop.entity.clash_royal.CRPlayerEntity;
import com.krazytop.service.clash_royal.CRPlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CRPlayerControllerTest {

    @InjectMocks
    private CRPlayerController playerController;

    @Mock
    private CRPlayerService playerService;

    @Test
    void testGetLocalPlayer_OK() {
        when(playerService.getLocalPlayer(anyString())).thenReturn(new CRPlayerEntity());
        ResponseEntity<CRPlayerEntity> response = playerController.getLocalPlayer("playerId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(playerService, times(1)).getLocalPlayer(anyString());
    }

    @Test
    void testGetLocalPlayer_NO_CONTENT() {
        when(playerService.getLocalPlayer(anyString())).thenReturn(null);
        ResponseEntity<CRPlayerEntity> response = playerController.getLocalPlayer("playerId");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(playerService, times(1)).getLocalPlayer(anyString());
    }

    @Test
    void testGetRemotePlayer_OK() throws IOException {
        when(playerService.getRemotePlayer(anyString())).thenReturn(new CRPlayerEntity());
        ResponseEntity<CRPlayerEntity> response = playerController.getRemotePlayer("playerId");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(playerService, times(1)).getRemotePlayer(anyString());

    }

    @Test
    void testGetRemotePlayer_NO_CONTENT() throws IOException {
        when(playerService.getRemotePlayer(anyString())).thenReturn(null);
        ResponseEntity<CRPlayerEntity> response = playerController.getRemotePlayer("playerId");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(playerService, times(1)).getRemotePlayer(anyString());

    }

    @Test
    void testGetRemotePlayer_ERROR() throws IOException {
        when(playerService.getRemotePlayer(anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<CRPlayerEntity> response = playerController.getRemotePlayer("playerId");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(playerService, times(1)).getRemotePlayer(anyString());

    }

    @Test
    void testUpdateRemoteToLocalPlayer_OK() throws IOException {
        ResponseEntity<String> response = playerController.updateRemoteToLocalPlayer(anyString());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(playerService, times(1)).updateRemoteToLocalPlayer(anyString());
    }

    @Test
    void testUpdateRemoteToLocalPlayer_ERROR() throws IOException {
        doThrow(RuntimeException.class).when(playerService).updateRemoteToLocalPlayer(anyString());
        ResponseEntity<String> response = playerController.updateRemoteToLocalPlayer("playerId");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(playerService, times(1)).updateRemoteToLocalPlayer(anyString());

    }
}