package com.krazytop.controller.lol;

import com.krazytop.service.lol.LOLStatsService;
import com.krazytop.service.tft.TFTStatsService;
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

    @Autowired
    private LOLStatsService lolStatsService;

    @GetMapping("/lol/stats/latest-matches-placement/{puuid}/{queue}/{role}")
    public ResponseEntity<List<String>> getLatestMatchesResult(@PathVariable String puuid, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieval results from last matches with PUUID : {}, queue type : {} and role : {}", puuid, queue, role);
        List<String> latestMatchesResults = lolStatsService.getLatestMatchesResult(puuid, queue, role);
        LOGGER.info("Recovered results : {}", latestMatchesResults);
        return new ResponseEntity<>(latestMatchesResults, HttpStatus.OK);
    }

}