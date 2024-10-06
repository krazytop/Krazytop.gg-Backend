package com.krazytop.controller.api_key;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        return setApiKey(key, GameEnum.RIOT);
    }

    @PostMapping("/api-key/clash-royal/{key}")
    public ResponseEntity<String> setCRApiKey(@PathVariable String key) {
        return setApiKey(key, GameEnum.CLASH_ROYAL);
    }

    public ResponseEntity<String> setApiKey(String key, GameEnum game) {
        LOGGER.info("Updating {} API key", game);
        try {
            apiKeyRepository.save(new ApiKeyEntity(game, key));
            LOGGER.info("{} API key successfully updated", game);
            return new ResponseEntity<>(String.format("%s API key successfully updated", game), HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating {} API key : {}", game, e.getMessage());
            return new ResponseEntity<>(String.format("An error occurred while updating %s API key", game), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}