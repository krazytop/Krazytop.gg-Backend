package com.krazytop.controller.lol;

import com.krazytop.nomenclature_management.LOLNomenclatureManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class LOLNomenclatureManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureManagementController.class);

    private final LOLNomenclatureManagement nomenclatureManagement;

    @Autowired
    public LOLNomenclatureManagementController(LOLNomenclatureManagement nomenclatureManagement){
        this.nomenclatureManagement = nomenclatureManagement;
    }

    @PostMapping("/lol/nomenclature")
    public ResponseEntity<HttpStatus> updateNomenclatures() {
        LOGGER.info("Updating all lol nomenclature");
        try {
            this.nomenclatureManagement.checkNomenclaturesToUpdate();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Error while updating lol nomenclatures : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}