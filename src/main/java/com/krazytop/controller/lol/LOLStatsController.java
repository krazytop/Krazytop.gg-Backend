package com.krazytop.controller.lol;

import com.krazytop.service.lol.LOLStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LOLStatsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLStatsController.class);

    private final LOLStatsService lolStatsService;

    @Autowired
    public LOLStatsController(LOLStatsService lolStatsService){
        this.lolStatsService = lolStatsService;
    }

    @GetMapping("/lol/stats/latest-matches-placement/{puuid}/{queue}/{role}")
    public ResponseEntity<List<String>> getLatestMatchesResult(@PathVariable String puuid, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving last LOL matches results");
        try {
            List<String> latestMatchesResults = lolStatsService.getLatestMatchesResult(puuid, queue, role);
            LOGGER.info("Last LOL matches results Retrieved");
            return new ResponseEntity<>(latestMatchesResults, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving last LOL matches results : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}