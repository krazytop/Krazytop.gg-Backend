package com.krazytop.controller.destiny;

import com.krazytop.service.destiny.DestinyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DestinyAuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyAuthController.class);

    private final DestinyAuthService destinyAuthService;

    @Autowired
    public DestinyAuthController(DestinyAuthService destinyAuthService){
        this.destinyAuthService = destinyAuthService;
    }

    @GetMapping("/destiny/get/{code}")
    public ResponseEntity<String> getPlayerToken(@PathVariable String code) throws IOException {
        LOGGER.info("Retrieving BUNGIE player tokens with code");
        String playerToken = destinyAuthService.getPlayerToken(code);
        LOGGER.info("BUNGIE player tokens retrieved");
        return new ResponseEntity<>(playerToken, HttpStatus.OK);
    }

    @PostMapping("/destiny/update")
    public ResponseEntity<String> updatePlayerToken(@RequestBody String refreshToken) throws IOException {
        LOGGER.info("Updating BUNGIE player tokens with refresh token");
        String playerToken = destinyAuthService.updatePlayerToken(refreshToken);
        LOGGER.info("BUNGIE player tokens refreshed");
        return new ResponseEntity<>(playerToken, HttpStatus.OK);
    }

}