package com.krazytop.controller.clash_royal;

import com.krazytop.service.clash_royal.CRNomenclatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CRNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRNomenclatureController.class);

    private final CRNomenclatureService crNomenclatureService;

    @Autowired
    public CRNomenclatureController(CRNomenclatureService crNomenclatureService){
        this.crNomenclatureService = crNomenclatureService;
    }

    @PostMapping("/clash-royal/nomenclature/account-level")
    public ResponseEntity<Boolean> updateAccountLevelNomenclature() {
        LOGGER.info("Updating account level nomenclature");
        boolean success = crNomenclatureService.updateAccountLevelNomenclature();
        LOGGER.info("Account level nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/clash-royal/nomenclature/card")
    public ResponseEntity<Boolean> updateCardNomenclature() {
        LOGGER.info("Updating card nomenclature");
        boolean success = crNomenclatureService.updateCardNomenclature();
        LOGGER.info("Card nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/clash-royal/nomenclature/card-rarity")
    public ResponseEntity<Boolean> updateCardRarityNomenclature() {
        LOGGER.info("Updating card rarity nomenclature");
        boolean success = crNomenclatureService.updateCardRarityNomenclature();
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

    @PostMapping("/clash-royal/api-key")
    public ResponseEntity<Boolean> updateApiKey(@RequestBody String apiKey) {
        LOGGER.info("Updating api key");
        boolean success = crNomenclatureService.updateApiKey(apiKey);
        LOGGER.info("Api key updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

}