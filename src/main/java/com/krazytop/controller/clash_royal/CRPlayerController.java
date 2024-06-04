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

    @Autowired
    private CRPlayerService crPlayerService;

    @GetMapping("/clash-royal/player/local/{playerId}")
    public ResponseEntity<CRPlayerEntity> getLocalPlayer(@PathVariable String playerId) {
        LOGGER.info("Retrieving of local information from player : {}", playerId);
        CRPlayerEntity player = crPlayerService.getLocalPlayer(playerId);
        LOGGER.info("Recovered player : {}", player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @GetMapping("/clash-royal/player/remote/{playerId}")
    public ResponseEntity<CRPlayerEntity> getRemotePlayer(@PathVariable String playerId) {
        LOGGER.info("Retrieving of remote information from player : {}", playerId);
        CRPlayerEntity player = crPlayerService.getRemotePlayer(playerId);
        LOGGER.info("Recovered player : {}", player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/clash-royal/player/update/{playerId}")
    public ResponseEntity<CRPlayerEntity> updateRemoteToLocalPlayer(@PathVariable String playerId) {
        LOGGER.info("Retrieving of local information from player : {}", playerId);
        CRPlayerEntity player = crPlayerService.updateRemoteToLocalPlayer(playerId);
        LOGGER.info("Recovered player : {}", player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

}