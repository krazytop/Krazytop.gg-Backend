package com.krazytop.controller.lol;

import com.krazytop.nomenclature_management.LOLNomenclatureManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LOLNomenclatureManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureManagementController.class);

    private final LOLNomenclatureManagement nomenclatureManagement;

    @Autowired
    public LOLNomenclatureManagementController(LOLNomenclatureManagement nomenclatureManagement){
        this.nomenclatureManagement = nomenclatureManagement;
    }

    @GetMapping("/lol/nomenclature")
    public ResponseEntity<String> updateNomenclatures() {
        LOGGER.info("Updating all LOL nomenclatures");
        try {
            if (this.nomenclatureManagement.updateAllNomenclatures()) {
                return new ResponseEntity<>("All LOL nomenclatures are successfully updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("All LOL nomenclatures are already up to date", HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error("Error while updating all LOL nomenclatures : {}", e.getMessage());
            return new ResponseEntity<>("Error while updating all LOL nomenclatures", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}