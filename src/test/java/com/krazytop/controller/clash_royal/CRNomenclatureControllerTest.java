package com.krazytop.controller.clash_royal;

import com.krazytop.service.clash_royal.CRNomenclatureService;
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
class CRNomenclatureControllerTest {

    @InjectMocks
    private CRNomenclatureController nomenclatureController;
    @Mock
    private CRNomenclatureService nomenclatureService;

    @Test
    void testUpdateNomenclature_OK() throws IOException, URISyntaxException {
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
    }

    @Test
    void testUpdateNomenclature_ERROR() throws IOException, URISyntaxException {
        doThrow(RuntimeException.class).when(nomenclatureService).updateAllNomenclatures();
        ResponseEntity<String> response = nomenclatureController.updateNomenclatures();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(nomenclatureService, times(1)).updateAllNomenclatures();
    }
}