package com.krazytop.controller.lol;

import com.krazytop.service.lol.LOLNomenclatureService;
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
class LOLNomenclatureControllerTest {

    @InjectMocks
    private LOLNomenclatureController nomenclatureController;
    @Mock
    private LOLNomenclatureService nomenclatureService;

    @Test
    void testUpdateNomenclature_OK_NEED() throws IOException, URISyntaxException {
        when(nomenclatureService.updateAllNomenclatures()).thenReturn(true);
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
    }

    @Test
    void testUpdateNomenclature_OK_NO_NEED() throws IOException, URISyntaxException {
        when(nomenclatureService.updateAllNomenclatures()).thenReturn(false);
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
    }

    @Test
    void testUpdateNomenclature_ERROR() throws IOException, URISyntaxException {
        when(nomenclatureService.updateAllNomenclatures()).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
    }
}