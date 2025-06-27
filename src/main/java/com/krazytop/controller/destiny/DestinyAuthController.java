package com.krazytop.controller.destiny;

import com.krazytop.api_gateway.api.generated.DestinyAuthentificationApi;
import com.krazytop.api_gateway.model.generated.DestinyAuthTokensDTO;
import com.krazytop.service.destiny.DestinyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DestinyAuthController implements DestinyAuthentificationApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyAuthController.class);

    private final DestinyAuthService destinyAuthService;

    @Autowired
    public DestinyAuthController(DestinyAuthService destinyAuthService){
        this.destinyAuthService = destinyAuthService;
    }

    @Override
    public ResponseEntity<DestinyAuthTokensDTO> getPlayerTokens(String code) {
        LOGGER.info("Retrieving BUNGIE player tokens with code");
        DestinyAuthTokensDTO playerTokens = destinyAuthService.getPlayerTokens(code);
        LOGGER.info("BUNGIE player tokens retrieved");
        return new ResponseEntity<>(playerTokens, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DestinyAuthTokensDTO> updatePlayerTokens(DestinyAuthTokensDTO tokens) {
        LOGGER.info("Updating BUNGIE player tokens with refresh token");
        DestinyAuthTokensDTO newTokens = destinyAuthService.updatePlayerTokens(tokens.getRefreshToken());
        LOGGER.info("BUNGIE player tokens refreshed");
        return new ResponseEntity<>(newTokens, HttpStatus.OK);
    }

}