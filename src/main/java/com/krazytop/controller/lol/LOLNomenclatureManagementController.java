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

    private final LOLNomenclatureManagement lolNomenclatureManagement;

    @Autowired
    public LOLNomenclatureManagementController(LOLNomenclatureManagement lolNomenclatureManagement){
        this.lolNomenclatureManagement = lolNomenclatureManagement;
    }

    @PostMapping("/lol/nomenclature")
    public ResponseEntity<HttpStatus> addAllNomenclature() {
        LOGGER.info("Updating all lol nomenclature");
        try {
            this.lolNomenclatureManagement.checkNomenclaturesToUpdate();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("Error while updating lol nomenclatures : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}