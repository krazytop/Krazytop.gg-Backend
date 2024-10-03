package com.krazytop.controller.api_key;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {

    @InjectMocks
    private ApiKeyController apiKeyController;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Test
    void tesGetRIOTApiKey_OK() {
        when(apiKeyRepository.findAll()).thenReturn(List.of(new ApiKeyEntity(GameEnum.RIOT, "API_KEY")));
        ResponseEntity<List<ApiKeyEntity>> response = apiKeyController.getAllApiKeys();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        verify(apiKeyRepository, times(1)).findAll();
    }

    @Test
    void tesGetRIOTApiKey_ERROR() {
        when(apiKeyRepository.findAll()).thenThrow(RuntimeException.class);
        ResponseEntity<List<ApiKeyEntity>> response = apiKeyController.getAllApiKeys();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(apiKeyRepository, times(1)).findAll();
    }

    @Test
    void testSetRIOTApiKey_OK() {
        ResponseEntity<String> response = apiKeyController.setRIOTApiKey("API_KEY");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
    }

    @Test
    void testSetRIOTApiKey_ERROR() {
        when(apiKeyRepository.save(any())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = apiKeyController.setRIOTApiKey("API_KEY");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
    }

    @Test
    void testSetCRApiKey_OK() {
        ResponseEntity<String> response = apiKeyController.setCRApiKey("API_KEY");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
    }

    @Test
    void testSetCRApiKey_ERROR() {
        when(apiKeyRepository.save(any())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = apiKeyController.setCRApiKey("API_KEY");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
    }
}