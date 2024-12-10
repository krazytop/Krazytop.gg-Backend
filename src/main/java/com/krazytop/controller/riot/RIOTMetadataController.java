package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.service.riot.RIOTMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RIOTMetadataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTMetadataController.class);

    private final RIOTMetadataService metadataService;

    @Autowired
    public RIOTMetadataController(RIOTMetadataService metadataService){
        this.metadataService = metadataService;
    }

    @GetMapping("/riot/metadata")
    public ResponseEntity<RIOTMetadataEntity> getMetadata() {
        LOGGER.info("Retrieving RIOT metadata");
        try {
            RIOTMetadataEntity metadata = metadataService.getMetadata();
            LOGGER.info("RIOT metadata retrieved");
            return new ResponseEntity<>(metadata, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving RIOT metadata : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}