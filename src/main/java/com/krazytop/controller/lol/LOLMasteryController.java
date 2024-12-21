package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMasteryEntity;
import com.krazytop.service.lol.LOLMasteryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class LOLMasteryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMasteryController.class);

    private final LOLMasteryService masteryService;

    @Autowired
    public LOLMasteryController(LOLMasteryService masteryService) {
        this.masteryService = masteryService;
    }

    @GetMapping("/lol/masteries/{puuid}")
    public ResponseEntity<List<LOLMasteryEntity>> getLocalMasteries(@PathVariable String puuid) {
        LOGGER.info("Retrieving LOL local masteries");
        List<LOLMasteryEntity> masteries = masteryService.getLocalMasteries(puuid);
        LOGGER.info("LOL local masteries retrieved");
        return new ResponseEntity<>(masteries, HttpStatus.OK);
    }

    @PostMapping("/lol/masteries/{puuid}")
    public ResponseEntity<String> updateRemoteToLocalMasteries(@PathVariable String puuid) throws URISyntaxException, IOException {
        LOGGER.info("Updating LOL masteries");
        masteryService.updateRemoteToLocalMasteries(puuid);
        LOGGER.info("LOL masteries successfully updated");
        return new ResponseEntity<>("LOL masteries successfully updated", HttpStatus.OK);
    }
}