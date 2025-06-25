package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LolApi;
import com.krazytop.api_gateway.model.generated.RIOTMetadataDTO;
import com.krazytop.service.lol.LOLMetadataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LOLMetadataController implements LolApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMetadataController.class);

    private final LOLMetadataService metadataService;

    @Autowired
    public LOLMetadataController(LOLMetadataService metadataService){
        this.metadataService = metadataService;
    }

    @Override
    public ResponseEntity<RIOTMetadataDTO> getMetadata() {
        LOGGER.info("Retrieving LOL metadata");
        RIOTMetadataDTO metadata = metadataService.getMetadata();
        LOGGER.info("LOL metadata retrieved");
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }
}