package com.krazytop.controller.clash_royal;

import com.krazytop.entity.clash_royal.CRPlayerEntity;
import com.krazytop.service.clash_royal.CRPlayerService;
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
public class CRPlayerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRPlayerController.class);

    private final CRPlayerService playerService;

    @Autowired
    public CRPlayerController(CRPlayerService playerService){
        this.playerService = playerService;
    }

    @GetMapping("/clash-royal/player/local/{playerId}")
    public ResponseEntity<CRPlayerEntity> getLocalPlayer(@PathVariable String playerId) {
        LOGGER.info("Retrieving CR local player");
        try {
            CRPlayerEntity player = playerService.getLocalPlayer(playerId);
            if (player != null) {
                LOGGER.info("CR local player retrieved");
                return new ResponseEntity<>(player, HttpStatus.OK);
            } else {
                LOGGER.info("CR local player not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving CR local summoner : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clash-royal/player/remote/{playerId}")
    public ResponseEntity<CRPlayerEntity> getRemotePlayer(@PathVariable String playerId) {
        LOGGER.info("Retrieving CR remote player");
        try {
            CRPlayerEntity player = playerService.getRemotePlayer(playerId);
            if (player != null) {
                LOGGER.info("CR remote player successfully retrieved");
                return new ResponseEntity<>(player, HttpStatus.OK);
            } else {
                LOGGER.info("CR remote player not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving CR remote player : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/clash-royal/player/update/{playerId}")
    public ResponseEntity<String> updateRemoteToLocalPlayer(@PathVariable String playerId) {
        LOGGER.info("Updating CR summoner");
        try {
            playerService.updateRemoteToLocalPlayer(playerId);
            LOGGER.info("CR player successfully updated");
            return new ResponseEntity<>("CR player successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating CR player : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating CR player", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}