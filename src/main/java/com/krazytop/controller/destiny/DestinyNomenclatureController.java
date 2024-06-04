package com.krazytop.controller.destiny;

import com.krazytop.nomenclature.destiny.*;
import com.krazytop.service.destiny.DestinyNomenclatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class DestinyNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyNomenclatureController.class);

    @Autowired
    private DestinyNomenclatureService destinyNomenclatureService;

    @PostMapping("/destiny/class")
    public ResponseEntity<Map<Long, DestinyClassNomenclature>> getClassNomenclatures(@RequestBody List<Long> classHashList) {
        LOGGER.info("Retrieving Destiny class nomenclatures for hash list: {}", classHashList);
        Map<Long, DestinyClassNomenclature> classNomenclatures = destinyNomenclatureService.getClassNomenclatures(classHashList);
        if (classNomenclatures.isEmpty()) {
            LOGGER.info("No class nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny class nomenclatures", classNomenclatures.size());
            return new ResponseEntity<>(classNomenclatures, HttpStatus.OK);
        }
    }

    @PostMapping("/destiny/vendors")
    public ResponseEntity<Map<Long, DestinyVendorNomenclature>> getVendorNomenclatures(@RequestBody List<Long> vendorHashList) {
        LOGGER.info("Retrieving Destiny vendor nomenclatures for hash list: {}", vendorHashList);
        Map<Long, DestinyVendorNomenclature> vendorNomenclatures = destinyNomenclatureService.getVendorNomenclatures(vendorHashList);
        if (vendorNomenclatures.isEmpty()) {
            LOGGER.info("No vendor nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny vendor nomenclatures", vendorNomenclatures.size());
            return new ResponseEntity<>(vendorNomenclatures, HttpStatus.OK);
        }
    }

    @PostMapping("/destiny/vendor-groups")
    public ResponseEntity<Map<Long, DestinyVendorGroupNomenclature>> getVendorGroupsNomenclatures(@RequestBody List<Long> vendorGroupHashList) {
        LOGGER.info("Retrieving Destiny vendor group nomenclatures for hash list: {}", vendorGroupHashList);
        Map<Long, DestinyVendorGroupNomenclature> vendorGroupNomenclatures = destinyNomenclatureService.getVendorGroupNomenclatures(vendorGroupHashList);
        if (vendorGroupNomenclatures.isEmpty()) {
            LOGGER.info("No vendor group nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny vendor group nomenclatures", vendorGroupNomenclatures.size());
            return new ResponseEntity<>(vendorGroupNomenclatures, HttpStatus.OK);
        }
    }

    @PostMapping("/destiny/progressions")
    public ResponseEntity<Map<Long, DestinyProgressionNomenclature>> getProgressionNomenclatures(@RequestBody List<Long> progressionHashList) {
        LOGGER.info("Retrieving Destiny progression nomenclatures for hash list: {}", progressionHashList);
        Map<Long, DestinyProgressionNomenclature> progressionNomenclatures = destinyNomenclatureService.getProgressionNomenclatures(progressionHashList);
        if (progressionNomenclatures.isEmpty()) {
            LOGGER.info("No progression nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny progression nomenclatures", progressionNomenclatures.size());
            return new ResponseEntity<>(progressionNomenclatures, HttpStatus.OK);
        }
    }

    @PostMapping("/destiny/items")
    public ResponseEntity<Map<Long, DestinyItemNomenclature>> getItemNomenclatures(@RequestBody List<Long> itemHashList) {
        LOGGER.info("Retrieving Destiny item nomenclatures for hash list: {}", itemHashList);
        Map<Long, DestinyItemNomenclature> itemNomenclatures = destinyNomenclatureService.getItemNomenclatures(itemHashList);
        if (itemNomenclatures.isEmpty()) {
            LOGGER.info("No item nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny item nomenclatures", itemNomenclatures.size());
            return new ResponseEntity<>(itemNomenclatures, HttpStatus.OK);
        }
    }

    @PostMapping("/destiny/records")
    public ResponseEntity<Map<Long, DestinyRecordNomenclature>> getRecordNomenclatures(@RequestBody List<Long> recordHashList) {
        LOGGER.info("Retrieving Destiny record nomenclatures for hash list: {}", recordHashList);
        Map<Long, DestinyRecordNomenclature> recordNomenclatures = destinyNomenclatureService.getRecordNomenclatures(recordHashList);
        if (recordNomenclatures.isEmpty()) {
            LOGGER.info("No record nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny record nomenclatures", recordNomenclatures.size());
            return new ResponseEntity<>(recordNomenclatures, HttpStatus.OK);
        }
    }

    @PostMapping("/destiny/trees")
    public ResponseEntity<Map<Long, DestinyPresentationTreeNomenclature>> getPresentationTreeNomenclatures(@RequestBody List<Long> presentationTreeHashList) {
        LOGGER.info("Retrieving Destiny presentation tree nomenclatures for hash list: {}", presentationTreeHashList);
        Map<Long, DestinyPresentationTreeNomenclature> presentationTreeNomenclatures = destinyNomenclatureService.getPresentationTreeNomenclatures(presentationTreeHashList);
        if (presentationTreeNomenclatures.isEmpty()) {
            LOGGER.info("No presentation tree nomenclatures found for the provided hash list");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            LOGGER.info("Retrieved {} Destiny presentation tree nomenclatures", presentationTreeNomenclatures.size());
            return new ResponseEntity<>(presentationTreeNomenclatures, HttpStatus.OK);
        }
    }

}