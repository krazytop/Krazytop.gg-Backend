package com.krazytop.controller.lol;

import com.krazytop.nomenclature_management.LOLNomenclatureManagement;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LOLNomenclatureManagementControllerTest {

    @InjectMocks
    private LOLNomenclatureManagementController nomenclatureManagementController;
    @Mock
    private LOLNomenclatureManagement nomenclatureManagement;

    @Test
    void addAllNomenclature() throws IOException, URISyntaxException {
       assertEquals(HttpStatus.OK, nomenclatureManagementController.updateNomenclatures().getStatusCode());
       verify(nomenclatureManagement, times(1)).checkNomenclaturesToUpdate();
    }
}