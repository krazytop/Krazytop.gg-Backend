package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.service.lol.LOLMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LOLMatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchController.class);

    private final LOLMatchService matchService;

    @Autowired
    public LOLMatchController(LOLMatchService matchService){
        this.matchService = matchService;
    }

    @GetMapping("/lol/matches/{puuid}/{pageNb}/{queue}/{role}")
    public ResponseEntity<List<LOLMatchEntity>> getLocalMatches(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving LOL local matches");
        try {
            List<LOLMatchEntity> matches = matchService.getLocalMatches(puuid, pageNb, queue, role);
            LOGGER.info("LOL local matches successfully retrieved");
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving LOL matches : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/lol/matches/count/{puuid}/{queue}/{role}")
    public ResponseEntity<Long> getLocalMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving LOL local matches count");
        try {
            Long matchesCount = matchService.getLocalMatchesCount(puuid, queue, role);
            LOGGER.info("LOL local matches count successfully retrieved");
            return new ResponseEntity<>(matchesCount, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving LOL local matches count : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/lol/matches/{puuid}")
    public ResponseEntity<String> updateRemoteToLocalMatches(@PathVariable String puuid) {
        LOGGER.info("Updating LOL matches");
        try {
            matchService.updateRemoteToLocalMatches(puuid);
            LOGGER.info("LOL matches successfully updated");
            return new ResponseEntity<>("LOL matches successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating LOL matches : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating LOL matches", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}