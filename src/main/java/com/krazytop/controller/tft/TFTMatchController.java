package com.krazytop.controller.tft;

import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.service.tft.TFTMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
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
    public ResponseEntity<List<TFTMatchEntity>> getMatches(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String queue, @PathVariable int set) {
        LOGGER.info("Retrieving TFT local matches");
        List<TFTMatchEntity> matches = matchService.getMatches(puuid, pageNb, queue, set);
        LOGGER.info("TFT local matches successfully retrieved");
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/tft/matches/count/{puuid}/{queue}/{set}")
    public ResponseEntity<Long> getMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable int set) {
        LOGGER.info("Retrieving TFT local matches count");
        Long matchesCount = matchService.getMatchesCount(puuid, queue, set);
        LOGGER.info("TFT local matches count successfully retrieved");
        return new ResponseEntity<>(matchesCount, HttpStatus.OK);
    }

    @PostMapping("/tft/matches/{puuid}")
    public ResponseEntity<String> updateMatches(@PathVariable String puuid) throws IOException, URISyntaxException, InterruptedException {
        LOGGER.info("Updating TFT matches");
        matchService.updateMatches(puuid);
        LOGGER.info("TFT matches successfully updated");
        return new ResponseEntity<>("TFT matches successfully updated", HttpStatus.OK);
    }

}