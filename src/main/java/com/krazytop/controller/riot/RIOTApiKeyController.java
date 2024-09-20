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
        RIOTApiKeyEntity apiKey = riotApiKeyRepository.findFirstByOrderByKeyAsc();
        LOGGER.info("Api key recovered");
        return new ResponseEntity<>(apiKey, HttpStatus.OK);
    }

    @PostMapping("/riot/api-key/{key}")
    public ResponseEntity<String> setApiKey(@PathVariable String key) {
        LOGGER.info("Updating api key");
        riotApiKeyRepository.deleteAll();
        riotApiKeyRepository.save(new RIOTApiKeyEntity(key));
        LOGGER.info("Api key updated");
        return new ResponseEntity<>("Api key is successfully updated", HttpStatus.OK);
    }

}