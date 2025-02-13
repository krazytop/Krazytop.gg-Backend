package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.service.riot.RIOTMetadataService;
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
class RIOTMetadataControllerTest {

    @InjectMocks
    private RIOTMetadataController metadataController;

    @Mock
    private RIOTMetadataService metadataService;

    @Test
    void testGetLocalSummoner_OK() {
        when(metadataService.getMetadata()).thenReturn(new RIOTMetadataEntity());

        ResponseEntity<RIOTMetadataEntity> response = metadataController.getMetadata();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(metadataService, times(1)).getMetadata();
    }

    @Test
    void testGetLocalSummoner_ERROR() {
        when(metadataService.getMetadata()).thenThrow(RuntimeException.class);

        ResponseEntity<RIOTMetadataEntity> response = metadataController.getMetadata();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
        verify(metadataService, times(1)).getMetadata();
    }
}