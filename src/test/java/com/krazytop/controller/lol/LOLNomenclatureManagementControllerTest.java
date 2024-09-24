package com.krazytop.controller.lol;

import com.krazytop.nomenclature_management.LOLNomenclatureManagement;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LOLNomenclatureManagementControllerTest {

    @InjectMocks
    private LOLNomenclatureManagementController nomenclatureManagementController;
    @Mock
    private LOLNomenclatureManagement nomenclatureManagement;

    @Test
    void testUpdateNomenclature_OK_NEED() throws IOException, URISyntaxException {
        when(nomenclatureManagement.updateAllNomenclatures()).thenReturn(true);
        ResponseEntity<String> response = nomenclatureManagementController.updateNomenclatures();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureManagement, times(1)).updateAllNomenclatures();
    }

    @Test
    void testUpdateNomenclature_OK_NO_NEED() throws IOException, URISyntaxException {
        when(nomenclatureManagement.updateAllNomenclatures()).thenReturn(false);
        ResponseEntity<String> response = nomenclatureManagementController.updateNomenclatures();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureManagement, times(1)).updateAllNomenclatures();
    }

    @Test
    void testUpdateNomenclature_ERROR() throws IOException, URISyntaxException {
        when(nomenclatureManagement.updateAllNomenclatures()).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = nomenclatureManagementController.updateNomenclatures();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureManagement, times(1)).updateAllNomenclatures();
    }
}