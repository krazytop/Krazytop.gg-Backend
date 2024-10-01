package com.krazytop.controller.api_key;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiKeyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyController.class);

    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public ApiKeyController(ApiKeyRepository apiKeyRepository){
        this.apiKeyRepository = apiKeyRepository;
    }

    @GetMapping("/api-key")
    public ResponseEntity<List<ApiKeyEntity>> getAllApiKeys() {
        LOGGER.info("Retrieving all API keys");
        try {
            List<ApiKeyEntity> apiKeys = apiKeyRepository.findAll();
            LOGGER.info("All API key successfully retrieved");
            return new ResponseEntity<>(apiKeys, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving all API keys : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api-key/riot/{key}")
    public ResponseEntity<String> setRIOTApiKey(@PathVariable String key) {
        LOGGER.info("Updating RIOT API key");
        try {
            apiKeyRepository.save(new ApiKeyEntity(GameEnum.RIOT, key));
            LOGGER.info("RIOT API key successfully updated");
            return new ResponseEntity<>("RIOT API key successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating RIOT API key : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating RIOT API key", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api-key/clash-royal/{key}")
    public ResponseEntity<String> setCRApiKey(@PathVariable String key) {
        LOGGER.info("Updating CR API key");
        try {
            apiKeyRepository.save(new ApiKeyEntity(GameEnum.CLASH_ROYAL, key));
            LOGGER.info("CR API key successfully updated");
            return new ResponseEntity<>("CR API key successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating CR API key : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating CR API key", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}