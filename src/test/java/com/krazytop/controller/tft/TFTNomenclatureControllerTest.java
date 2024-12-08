package com.krazytop.controller.tft;

import com.krazytop.service.tft.TFTNomenclatureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TFTNomenclatureControllerTest {

    @InjectMocks
    private TFTNomenclatureController nomenclatureController;

    @Mock
    private TFTNomenclatureService nomenclatureService;

    @Test
    void testUpdateNomenclature_OK_NEED() throws IOException, URISyntaxException {
        when(nomenclatureService.updateAllNomenclatures()).thenReturn(true);
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures(false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
        verify(nomenclatureService, times(0)).updateLegacyNomenclatures();
    }

    @Test
    void testUpdateNomenclature_OK_NO_NEED() throws IOException, URISyntaxException {
        when(nomenclatureService.updateAllNomenclatures()).thenReturn(false);
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures(false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
        verify(nomenclatureService, times(0)).updateLegacyNomenclatures();
    }

    @Test
    void testUpdateNomenclature_ERROR() throws IOException, URISyntaxException {
        when(nomenclatureService.updateAllNomenclatures()).thenThrow(RuntimeException.class);
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures(true);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
        verify(nomenclatureService, times(1)).updateLegacyNomenclatures();
    }
}