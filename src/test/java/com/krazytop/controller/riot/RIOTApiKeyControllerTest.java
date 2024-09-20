package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTApiKeyEntity;
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
class RIOTApiKeyControllerTest {

    @InjectMocks
    private RIOTApiKeyController apiKeyController;
    @Mock
    private RIOTApiKeyRepository apiKeyRepository;

    @Test
    void tesGetApiKey_OK() {
        when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getApiKey();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void tesGetApiKey_NO_CONTENT() {
        when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(null);
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getApiKey();
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void tesGetApiKey_ERROR() {
        when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenThrow(RuntimeException.class);
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getApiKey();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void testSetApiKey_OK() {
        ResponseEntity<String> response = apiKeyController.setApiKey("API_KEY");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
        verify(apiKeyRepository, times(1)).deleteAll();
    }

    @Test
    void testSetApiKey_ERROR() {
        when(apiKeyRepository.save(any())).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = apiKeyController.setApiKey("API_KEY");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
        verify(apiKeyRepository, times(1)).deleteAll();
    }
}