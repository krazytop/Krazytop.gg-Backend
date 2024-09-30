package com.krazytop.controller;

import com.krazytop.entity.clash_royal.CRApiKeyEntity;
import com.krazytop.entity.riot.RIOTApiKeyEntity;
import com.krazytop.repository.clash_royal.CRApiKeyRepository;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {

    @InjectMocks
    private ApiKeyController apiKeyController;
    @Mock
    private RIOTApiKeyRepository riotApiKeyRepository;
    @Mock
    private CRApiKeyRepository crApiKeyRepository;

    @Test
    void tesGetRIOTApiKey_OK() {
        when(riotApiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getRIOTApiKey();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(riotApiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void tesGetRIOTApiKey_NO_CONTENT() {
        when(riotApiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(null);
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getRIOTApiKey();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(riotApiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void tesGetRIOTApiKey_ERROR() {
        when(riotApiKeyRepository.findFirstByOrderByKeyAsc()).thenThrow(RuntimeException.class);
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getRIOTApiKey();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(riotApiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void testSetRIOTApiKey_OK() {
        ResponseEntity<String> response = apiKeyController.setRIOTApiKey("API_KEY");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(riotApiKeyRepository, times(1)).save(any());
        verify(riotApiKeyRepository, times(1)).deleteAll();
    }

    @Test
    void testSetRIOTApiKey_ERROR() {
        when(riotApiKeyRepository.save(any())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = apiKeyController.setRIOTApiKey("API_KEY");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(riotApiKeyRepository, times(1)).save(any());
        verify(riotApiKeyRepository, times(1)).deleteAll();
    }

    @Test
    void tesGetCRApiKey_OK() {
        when(crApiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new CRApiKeyEntity("API_KEY"));
        ResponseEntity<CRApiKeyEntity> response = apiKeyController.getCRApiKey();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(crApiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void tesGetCRApiKey_NO_CONTENT() {
        when(crApiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(null);
        ResponseEntity<CRApiKeyEntity> response = apiKeyController.getCRApiKey();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(crApiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void tesGetCRApiKey_ERROR() {
        when(crApiKeyRepository.findFirstByOrderByKeyAsc()).thenThrow(RuntimeException.class);
        ResponseEntity<CRApiKeyEntity> response = apiKeyController.getCRApiKey();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(crApiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void testSetCRApiKey_OK() {
        ResponseEntity<String> response = apiKeyController.setCRApiKey("API_KEY");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(crApiKeyRepository, times(1)).save(any());
        verify(crApiKeyRepository, times(1)).deleteAll();
    }

    @Test
    void testSetCRApiKey_ERROR() {
        when(crApiKeyRepository.save(any())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = apiKeyController.setCRApiKey("API_KEY");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(crApiKeyRepository, times(1)).save(any());
        verify(crApiKeyRepository, times(1)).deleteAll();
    }
}