package com.krazytop.controller.tft;

import com.krazytop.api_gateway.api.generated.TftApi;
import com.krazytop.api_gateway.model.generated.RIOTMetadataDTO;
import com.krazytop.service.lol.LOLMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TFTMetadataController implements TftApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTMetadataController.class);

    private final LOLMetadataService metadataService;

    @Autowired
    public TFTMetadataController(LOLMetadataService metadataService){
        this.metadataService = metadataService;
    }

    @Override
    public ResponseEntity<RIOTMetadataDTO> getMetadata() {
        LOGGER.info("Retrieving TFT metadata");
        RIOTMetadataDTO metadata = metadataService.getMetadata();
        LOGGER.info("TFT metadata retrieved");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }
}