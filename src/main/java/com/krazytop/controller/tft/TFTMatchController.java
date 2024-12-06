package com.krazytop.controller.tft;

import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.service.tft.TFTMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TFTMatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTMatchController.class);

    private final TFTMatchService matchService;

    @Autowired
    public TFTMatchController(TFTMatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/tft/matches/{puuid}/{pageNb}/{queue}/{set}")
    public ResponseEntity<List<TFTMatchEntity>> getLocalMatches(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String queue, @PathVariable int set) {
        LOGGER.info("Retrieving TFT local matches");
        try {
            List<TFTMatchEntity> matches = matchService.getLocalMatches(puuid, pageNb, queue, set);
            LOGGER.info("TFT local matches successfully retrieved");
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving TFT matches : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tft/matches/count/{puuid}/{queue}/{set}")
    public ResponseEntity<Long> getLocalMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable int set) {
        LOGGER.info("Retrieving TFT local matches count");
        try {
            Long matchesCount = matchService.getLocalMatchesCount(puuid, queue, set);
            LOGGER.info("TFT local matches count successfully retrieved");
            return new ResponseEntity<>(matchesCount, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving TFT local matches count : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tft/matches/{puuid}")
    public ResponseEntity<String> updateRemoteToLocalMatches(@PathVariable String puuid, @RequestParam(required = false, defaultValue = "false") boolean force) {
        LOGGER.info("Updating TFT matches");
        try {
            matchService.updateRemoteToLocalMatches(puuid, 0, force);
            LOGGER.info("TFT matches successfully updated");
            return new ResponseEntity<>("TFT matches successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating TFT matches : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating TFT matches", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}