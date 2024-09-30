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
        LOGGER.info("Retrieving BUNGIE player tokens with code");
        try {
            String playerToken = destinyAuthService.getPlayerToken(code);
            LOGGER.info("BUNGIE player tokens retrieved");
            return new ResponseEntity<>(playerToken, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error while retrieving BUNGIE player tokens : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/destiny/update")
    public ResponseEntity<String> updatePlayerToken(@RequestBody Map<String, String> requestBody) {
        LOGGER.info("Updating BUNGIE player tokens with refresh token");
        try {
            String playerToken = destinyAuthService.updatePlayerToken(requestBody.get("refreshToken"));
            LOGGER.info("BUNGIE player tokens refreshed");
            return new ResponseEntity<>(playerToken, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Error while refreshing BUNGIE player tokens : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}