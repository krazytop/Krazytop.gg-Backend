package com.krazytop.controller.api_key;

import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiKeyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyController.class);

    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public ApiKeyController(ApiKeyRepository apiKeyRepository){
        this.apiKeyRepository = apiKeyRepository;
    }

    @PostMapping("/api-key/tft/{key}")
    public ResponseEntity<String> setTFTApiKey(@PathVariable String key) {
        return setApiKey(key, GameEnum.TFT);
    }

    @PostMapping("/api-key/lol/{key}")
    public ResponseEntity<String> setLOLApiKey(@PathVariable String key) {
        return setApiKey(key, GameEnum.LOL);
    }

    @PostMapping("/api-key/clash-royal/{key}")
    public ResponseEntity<String> setCRApiKey(@PathVariable String key) {
        return setApiKey(key, GameEnum.CLASH_ROYAL);
    }

    public ResponseEntity<String> setApiKey(String key, GameEnum game) {
        LOGGER.info("Updating {} API key", game);
        apiKeyRepository.save(new ApiKeyEntity(game, key));
        LOGGER.info("{} API key successfully updated", game);
        return new ResponseEntity<>(String.format("%s API key successfully updated", game), HttpStatus.OK);
    }
}