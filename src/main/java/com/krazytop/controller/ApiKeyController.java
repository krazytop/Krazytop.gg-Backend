package com.krazytop.controller;

import com.krazytop.entity.clash_royal.CRApiKeyEntity;
import com.krazytop.entity.riot.RIOTApiKeyEntity;
import com.krazytop.repository.clash_royal.CRApiKeyRepository;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiKeyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyController.class);

    private final RIOTApiKeyRepository riotApiKeyRepository;
    private final CRApiKeyRepository crApiKeyRepository;

    @Autowired
    public ApiKeyController(RIOTApiKeyRepository riotApiKeyRepository, CRApiKeyRepository crApiKeyRepository) {
        this.riotApiKeyRepository = riotApiKeyRepository;
        this.crApiKeyRepository = crApiKeyRepository;
    }

    @GetMapping("/riot/api-key")
    public ResponseEntity<RIOTApiKeyEntity> getRIOTApiKey() {
        LOGGER.info("Retrieving RIOT api key");
        try {
            RIOTApiKeyEntity apiKey = riotApiKeyRepository.findFirstByOrderByKeyAsc();
            if (apiKey != null) {
                LOGGER.info("RIOT api key successfully retrieved");
                return new ResponseEntity<>(apiKey, HttpStatus.OK);
            } else {
                LOGGER.info("RIOT api key not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving RIOT api key : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/riot/api-key/{key}")
    public ResponseEntity<String> setRIOTApiKey(@PathVariable String key) {
        LOGGER.info("Updating RIOT api key");
        try {
            riotApiKeyRepository.deleteAll();
            riotApiKeyRepository.save(new RIOTApiKeyEntity(key));
            LOGGER.info("RIOT api key successfully updated");
            return new ResponseEntity<>("RIOT api key successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating RIOT api key : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating RIOT api key", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clash-royal/api-key")
    public ResponseEntity<CRApiKeyEntity> getCRApiKey() {
        LOGGER.info("Retrieving CR api key");
        try {
            CRApiKeyEntity apiKey = crApiKeyRepository.findFirstByOrderByKeyAsc();
            if (apiKey != null) {
                LOGGER.info("CR api key successfully retrieved");
                return new ResponseEntity<>(apiKey, HttpStatus.OK);
            } else {
                LOGGER.info("CR api key not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving CR api key : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/clash-royal/api-key/{key}")
    public ResponseEntity<String> setCRApiKey(@PathVariable String key) {
        LOGGER.info("Updating CR api key");
        try {
            crApiKeyRepository.deleteAll();
            crApiKeyRepository.save(new CRApiKeyEntity(key));
            LOGGER.info("CR api key successfully updated");
            return new ResponseEntity<>("CR api key successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating CR api key : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating CR api key", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}