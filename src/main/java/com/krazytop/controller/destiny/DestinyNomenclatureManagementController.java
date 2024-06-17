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


    @PostMapping("/destiny/nomenclature")
    public ResponseEntity<String> updateAllNomenclature() {
        LOGGER.info("Updating all nomenclature");
        try {
            String manifest = destinyNomenclatureManagement.downloadManifest();
            destinyNomenclatureManagement.updateClassNomenclature(manifest);
            destinyNomenclatureManagement.updateObjectiveNomenclature(manifest);
            destinyNomenclatureManagement.updateItemNomenclature(manifest);
            destinyNomenclatureManagement.updateVendorNomenclature(manifest);
            destinyNomenclatureManagement.updateVendorGroupNomenclature(manifest);
            destinyNomenclatureManagement.updateRecordNomenclature(manifest);
            destinyNomenclatureManagement.updateProgressionNomenclature(manifest);
            destinyNomenclatureManagement.updateCollectibleNomenclature(manifest);
            destinyNomenclatureManagement.updateMetricNomenclature(manifest);
            destinyNomenclatureManagement.updatePresentationNodeNomenclature(manifest);
            LOGGER.info("All nomenclature updated");
            return new ResponseEntity<>("All nomenclature updated", HttpStatus.OK);
        } catch (IOException | NullPointerException e) {
            return new ResponseEntity<>("An error occurred when when nomenclatures were updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}