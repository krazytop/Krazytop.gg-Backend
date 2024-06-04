package com.krazytop.controller.clash_royal;

import com.krazytop.nomenclature_management.CRNomenclatureManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CRNomenclatureManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRNomenclatureManagementController.class);

    @Autowired
    private CRNomenclatureManagement crNomenclatureManagement;

    @PostMapping("/clash-royal/nomenclature/account-level")
    public ResponseEntity<Boolean> updateAccountLevelNomenclature() {
        LOGGER.info("Updating account level nomenclature");
        boolean success = crNomenclatureManagement.updateAccountLevelNomenclature();
        LOGGER.info("Account level nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/clash-royal/nomenclature/card")
    public ResponseEntity<Boolean> updateCardNomenclature() {
        LOGGER.info("Updating card nomenclature");
        boolean success = crNomenclatureManagement.updateCardNomenclature();
        LOGGER.info("Card nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/clash-royal/nomenclature/card-rarity")
    public ResponseEntity<Boolean> updateCardRarityNomenclature() {
        LOGGER.info("Updating card rarity nomenclature");
        boolean success = crNomenclatureManagement.updateCardRarityNomenclature();
        LOGGER.info("Card rarity nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/clash-royal/nomenclature")
    public ResponseEntity<Boolean> addAllNomenclature() {
        LOGGER.info("Updating all nomenclature");
        boolean successAccountLevel = Boolean.TRUE.equals(this.updateAccountLevelNomenclature().getBody());
        boolean successCard = Boolean.TRUE.equals(this.updateCardNomenclature().getBody());
        boolean successCardRarity = Boolean.TRUE.equals(this.updateCardRarityNomenclature().getBody());
        LOGGER.info("All nomenclature updated");
        return new ResponseEntity<>(successAccountLevel && successCard && successCardRarity, HttpStatus.OK);
    }

}