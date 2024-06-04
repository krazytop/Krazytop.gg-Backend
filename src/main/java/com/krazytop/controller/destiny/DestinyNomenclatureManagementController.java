package com.krazytop.controller.destiny;

import com.krazytop.nomenclature_management.DestinyNomenclatureManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class DestinyNomenclatureManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyNomenclatureManagementController.class);

    @Autowired
    private DestinyNomenclatureManagement destinyNomenclatureManagement;

    @PostMapping("/destiny/nomenclature/manifest")
    public ResponseEntity<Boolean> updateManifest() {
        LOGGER.info("Updating destiny manifest");
        try {
            boolean manifestUpdated = destinyNomenclatureManagement.updateManifest();
            LOGGER.info("Destiny manifest updated");
            return new ResponseEntity<>(manifestUpdated, HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("Error updating destiny manifest");
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/class")
    public ResponseEntity<Boolean> updateClassNomenclature() {
        try {
            destinyNomenclatureManagement.updateClassNomenclature();
            LOGGER.info("Destiny class nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny class nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/vendor")
    public ResponseEntity<Boolean> updateVendorNomenclature() {
        try {
            destinyNomenclatureManagement.updateVendorNomenclature();
            LOGGER.info("Destiny vendor nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny vendor nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/vendor-group")
    public ResponseEntity<Boolean> updateVendorGroupNomenclature() {
        try {
            destinyNomenclatureManagement.updateVendorGroupNomenclature();
            LOGGER.info("Destiny vendor group nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny vendor group nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/record")
    public ResponseEntity<Boolean> updateRecordNomenclature() {
        try {
            destinyNomenclatureManagement.updateRecordNomenclature();
            LOGGER.info("Destiny record nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny record nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/progression")
    public ResponseEntity<Boolean> updateProgressionNomenclature() {
        try {
            destinyNomenclatureManagement.updateProgressionNomenclature();
            LOGGER.info("Destiny progression nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny progression nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/item")
    public ResponseEntity<Boolean> updateItemNomenclature() {
        try {
            destinyNomenclatureManagement.updateItemNomenclature();
            LOGGER.info("Destiny item nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny item nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/presentation-node")
    public ResponseEntity<Boolean> updatePresentationNodeNomenclature() {
        try {
            destinyNomenclatureManagement.updatePresentationNodeNomenclature();
            LOGGER.info("Destiny presentation node nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny presentation node nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/collectible")
    public ResponseEntity<Boolean> updateCollectibleNomenclature() {
        try {
            destinyNomenclatureManagement.updateCollectibleNomenclature();
            LOGGER.info("Destiny collectible nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny collectible nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/metric")
    public ResponseEntity<Boolean> updateMetricNomenclature() {
        try {
            destinyNomenclatureManagement.updateMetricNomenclature();
            LOGGER.info("Destiny metric nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny metric nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature/objective")
    public ResponseEntity<Boolean> updateObjectiveNomenclature() {
        try {
            destinyNomenclatureManagement.updateObjectiveNomenclature();
            LOGGER.info("Destiny objective nomenclature updated");
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error updating destiny objective nomenclature: {}", e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/nomenclature")
    public ResponseEntity<Boolean> updateAllNomenclature() {
        LOGGER.info("Updating all nomenclature");
        if (Boolean.TRUE.equals(this.updateManifest().getBody())) { //TODO faire tree en dernier et voir l ordre de tout
            boolean successClass = Boolean.TRUE.equals(this.updateClassNomenclature().getBody());
            boolean successObjective = Boolean.TRUE.equals(this.updateObjectiveNomenclature().getBody());
            boolean successItem = Boolean.TRUE.equals(this.updateItemNomenclature().getBody());
            boolean successVendor = Boolean.TRUE.equals(this.updateVendorNomenclature().getBody());
            boolean successVendorGroup = Boolean.TRUE.equals(this.updateVendorGroupNomenclature().getBody());
            boolean successRecord = Boolean.TRUE.equals(this.updateRecordNomenclature().getBody());
            boolean successProgression = Boolean.TRUE.equals(this.updateProgressionNomenclature().getBody());
            boolean successCollectible = Boolean.TRUE.equals(this.updateCollectibleNomenclature().getBody());
            boolean successMetric = Boolean.TRUE.equals(this.updateMetricNomenclature().getBody());
            boolean successPresentationNode = Boolean.TRUE.equals(this.updatePresentationNodeNomenclature().getBody());
            LOGGER.info("All nomenclature updated");
            return new ResponseEntity<>(successClass && successVendor && successObjective && successVendorGroup && successRecord && successProgression && successItem && successPresentationNode && successCollectible && successMetric, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}