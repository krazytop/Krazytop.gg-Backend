package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTApiKeyEntity;
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
public class RIOTApiKeyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTApiKeyController.class);

    private final RIOTApiKeyRepository riotApiKeyRepository;

    @Autowired
    public RIOTApiKeyController(RIOTApiKeyRepository riotApiKeyRepository) {
        this.riotApiKeyRepository = riotApiKeyRepository;
    }

    @GetMapping("/riot/api-key")
    public ResponseEntity<RIOTApiKeyEntity> getApiKey() {
        LOGGER.info("Retrieving api key");
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
    public ResponseEntity<String> setApiKey(@PathVariable String key) {
        LOGGER.info("Updating api key");
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

}