package com.krazytop.controller.clash_royal;

import com.krazytop.service.clash_royal.CRNomenclatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CRNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRNomenclatureController.class);

    private final CRNomenclatureService nomenclatureService;

    @Autowired
    public CRNomenclatureController(CRNomenclatureService crNomenclatureService){
        this.nomenclatureService = crNomenclatureService;
    }

    @GetMapping("/clash-royal/nomenclature")
    public ResponseEntity<String> updateNomenclatures() {
        LOGGER.info("Updating all CR nomenclatures");
        try {
            this.nomenclatureService.updateAllNomenclatures();
            return new ResponseEntity<>("All CR nomenclatures are successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error while updating all CR nomenclatures : {}", e.getMessage());
            return new ResponseEntity<>("Error while updating all CR nomenclatures", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}