package com.krazytop.controller.tft;

import com.krazytop.nomenclature_management.TFTNomenclatureManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TFTNomenclatureManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTNomenclatureManagementController.class);

    private final TFTNomenclatureManagement tftNomenclatureManagement;

    @Autowired
    public TFTNomenclatureManagementController(TFTNomenclatureManagement tftNomenclatureManagement) {
        this.tftNomenclatureManagement = tftNomenclatureManagement;
    }

    @PostMapping("/tft/nomenclature/trait")
    public ResponseEntity<Boolean> updateTraitNomenclature() {
        LOGGER.info("Updating tft trait nomenclature");
        boolean success = tftNomenclatureManagement.updateTraitNomenclature();
        LOGGER.info("TFT Trait nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/tft/nomenclature/unit")
    public ResponseEntity<Boolean> updateUnitNomenclature() {
        LOGGER.info("Updating tft unit nomenclature");
        boolean success = tftNomenclatureManagement.updateUnitNomenclature();
        LOGGER.info("TFT unit nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/tft/nomenclature/queue")
    public ResponseEntity<Boolean> updateQueueNomenclature() {
        LOGGER.info("Updating tft queue nomenclature");
        boolean success = tftNomenclatureManagement.updateQueueNomenclature();
        LOGGER.info("TFT Queue nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/tft/nomenclature/item")
    public ResponseEntity<Boolean> updateItemNomenclature() {
        LOGGER.info("Updating tft item nomenclature");
        boolean success = tftNomenclatureManagement.updateItemNomenclature();
        LOGGER.info("TFT item nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/tft/nomenclature/augment")
    public ResponseEntity<Boolean> updateAugmentNomenclature() {
        LOGGER.info("Updating tft augment nomenclature");
        boolean success = tftNomenclatureManagement.updateAugmentNomenclature();
        LOGGER.info("TFT augment nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/tft/nomenclature")
    public ResponseEntity<Boolean> addAllNomenclature() {
        LOGGER.info("Updating all tft nomenclature");
        boolean successTrait = Boolean.TRUE.equals(this.updateTraitNomenclature().getBody());
        boolean successUnit = Boolean.TRUE.equals(this.updateUnitNomenclature().getBody());
        boolean successQueue = Boolean.TRUE.equals(this.updateQueueNomenclature().getBody());
        boolean successItem = Boolean.TRUE.equals(this.updateItemNomenclature().getBody());
        boolean successAugment = Boolean.TRUE.equals(this.updateAugmentNomenclature().getBody());
        LOGGER.info("All tft nomenclature updated");
        return new ResponseEntity<>(successTrait && successUnit && successQueue && successItem && successAugment, HttpStatus.OK);
    }

}