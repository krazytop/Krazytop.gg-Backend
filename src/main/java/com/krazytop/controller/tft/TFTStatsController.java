package com.krazytop.controller.tft;

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
public class TFTStatsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTStatsController.class);

    private final TFTStatsService tftStatsService;

    @Autowired
    public TFTStatsController(TFTStatsService tftStatsService) {
        this.tftStatsService = tftStatsService;
    }

    @GetMapping("/tft/stats/latest-matches-placement/{puuid}/{set}/{queue}")
    public ResponseEntity<List<Integer>> getLatestMatchesPlacement(@PathVariable String puuid, @PathVariable String queue, @PathVariable String set) {
        LOGGER.info("Retrieving last matches placement");
        List<Integer> latestMatchesResults = tftStatsService.getLatestMatchesPlacement(puuid, queue, set);
        LOGGER.info("Last matches placement recovered");
        return new ResponseEntity<>(latestMatchesResults, HttpStatus.OK);
    }

    @GetMapping("/tft/stats/latest-matches-placement/{puuid}/{set}")
    public ResponseEntity<List<Integer>> getLatestMatchesPlacementWithoutQueueTypeRestriction(@PathVariable String puuid, @PathVariable String set) {
        return getLatestMatchesPlacement(puuid, null, set);
    }

}