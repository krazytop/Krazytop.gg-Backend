package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTApiKeyEntity;
import com.krazytop.service.riot.RIOTApiKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RIOTApiKeyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTApiKeyController.class);

    @Autowired
    private RIOTApiKeyService riotApiKeyService;

    @GetMapping("/riot/api-key")
    public ResponseEntity<RIOTApiKeyEntity> getApiKey() {
        LOGGER.info("Retrieving RIOT API key");
        RIOTApiKeyEntity apiKey = riotApiKeyService.getApiKey();
        LOGGER.info("Recovered RIOT API key : {}", apiKey);
        return new ResponseEntity<>(apiKey, HttpStatus.OK);
    }

}