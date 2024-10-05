package com.krazytop.controller.destiny;

import com.krazytop.service.destiny.DestinyAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DestinyAuthControllerTest {

    @InjectMocks
    private DestinyAuthController authController;

    @Mock
    private DestinyAuthService authService;

    @Test
    void testGetPlayerToken_OK() throws IOException {
        when(authService.getPlayerToken(anyString())).thenReturn("tokens");
        ResponseEntity<String> response = authController.getPlayerToken("code");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authService, times(1)).getPlayerToken(anyString());
    }

    @Test
    void testGetPlayerToken_ERROR() throws IOException {
        when(authService.getPlayerToken(anyString())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = authController.getPlayerToken("code");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(authService, times(1)).getPlayerToken(anyString());
    }

    @Test
    void testUpdatePlayerToken_OK() throws IOException {
        when(authService.updatePlayerToken(any())).thenReturn("tokens");
        ResponseEntity<String> response = authController.updatePlayerToken(new HashMap<>() {{put("refreshToken", "token");}});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(authService, times(1)).updatePlayerToken(any());
    }

    @Test
    void testUpdatePlayerToken_ERROR() throws IOException {
        when(authService.updatePlayerToken(any())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = authController.updatePlayerToken(new HashMap<>() {{put("refreshToken", "token");}});
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(authService, times(1)).updatePlayerToken(any());
    }
}