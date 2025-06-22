package com.krazytop.controller.destiny;

import com.krazytop.api_gateway.api.generated.DestinyApi;
import com.krazytop.service.destiny.DestinyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DestinyAuthController implements DestinyApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyAuthController.class);

    private final DestinyAuthService destinyAuthService;

    @Autowired
    public DestinyAuthController(DestinyAuthService destinyAuthService){
        this.destinyAuthService = destinyAuthService;
    }

    @Override
    public ResponseEntity<String> getPlayerToken(String code) {
        LOGGER.info("Retrieving BUNGIE player tokens with code");
        String playerToken = destinyAuthService.getPlayerToken(code);
        LOGGER.info("BUNGIE player tokens retrieved");
        return new ResponseEntity<>(playerToken, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updatePlayerToken(String refreshToken) {
        LOGGER.info("Updating BUNGIE player tokens with refresh token");
        String playerToken = destinyAuthService.updatePlayerToken(refreshToken);
        LOGGER.info("BUNGIE player tokens refreshed");
        return new ResponseEntity<>(playerToken, HttpStatus.OK);
    }

}