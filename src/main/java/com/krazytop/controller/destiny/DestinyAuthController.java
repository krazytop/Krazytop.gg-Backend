package com.krazytop.controller.destiny;

import com.krazytop.service.destiny.DestinyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DestinyAuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyAuthController.class);

    private final DestinyAuthService destinyAuthService;

    @Autowired
    public DestinyAuthController(DestinyAuthService destinyAuthService){
        this.destinyAuthService = destinyAuthService;
    }

    @GetMapping("/destiny/get/{code}")
    public ResponseEntity<String> getPlayerToken(@PathVariable String code) {
        LOGGER.info("Retrieving Destiny player token for code : {}", code);
        String playerToken = destinyAuthService.getPlayerToken(code);
        LOGGER.info("Recovered Destiny player token : {}", playerToken);
        return new ResponseEntity<>(playerToken, HttpStatus.OK);
    }

    @PostMapping("/destiny/update")
    public ResponseEntity<String> updatePlayerToken(@RequestBody Map<String, String> requestBody) {
        String refreshToken = requestBody.get("refreshToken");
        LOGGER.info("Updating Destiny player token for refresh token");
        String playerToken = destinyAuthService.updatePlayerToken(refreshToken);
        LOGGER.info("Destiny player token updated: {}", playerToken);
        return new ResponseEntity<>(playerToken, HttpStatus.OK);
    }

}