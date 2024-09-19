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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RIOTApiKeyControllerTest {

    @InjectMocks
    private RIOTApiKeyController apiKeyController;
    @Mock
    private RIOTApiKeyRepository apiKeyRepository;

    @Test
    void getApiKey() {
        when(apiKeyRepository.findFirstByOrderByKeyAsc()).thenReturn(new RIOTApiKeyEntity("API_KEY"));
        ResponseEntity<RIOTApiKeyEntity> response = apiKeyController.getApiKey();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).findFirstByOrderByKeyAsc();
    }

    @Test
    void setApiKey() {
        ResponseEntity<String> response = apiKeyController.setApiKey("API_KEY");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(apiKeyRepository, times(1)).save(any());
        verify(apiKeyRepository, times(1)).deleteAll();
    }
}