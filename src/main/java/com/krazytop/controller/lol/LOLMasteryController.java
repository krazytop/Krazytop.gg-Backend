package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMasteriesEntity;
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
import java.util.Optional;

@RestController
public class LOLMasteryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMasteryController.class);

    private final LOLMasteryService masteryService;

    @Autowired
    public LOLMasteryController(LOLMasteryService masteryService) {
        this.masteryService = masteryService;
    }

    @GetMapping("/lol/masteries/{puuid}")
    public ResponseEntity<LOLMasteriesEntity> getMasteries(@PathVariable String puuid) {
        LOGGER.info("Retrieving LOL local masteries");
        Optional<LOLMasteriesEntity> masteries = masteryService.getMasteries(puuid);
        if (masteries.isPresent()) {
            LOGGER.info("LOL local masteries retrieved");
            return new ResponseEntity<>(masteries.get(), HttpStatus.OK);
        } else {
            LOGGER.info("LOL local masteries not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/lol/masteries/{puuid}")
    public ResponseEntity<String> updateMasteries(@PathVariable String puuid) throws URISyntaxException, IOException {
        LOGGER.info("Updating LOL masteries");
        masteryService.updateMasteries(puuid);
        LOGGER.info("LOL masteries successfully updated");
        return new ResponseEntity<>("LOL masteries successfully updated", HttpStatus.OK);
    }
}