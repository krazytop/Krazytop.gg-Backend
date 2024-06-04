package com.krazytop.controller.tft;

import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.service.tft.TFTMatchService;
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
public class TFTMatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTMatchController.class);

    @Autowired
    private TFTMatchService tftMatchService;

    @GetMapping("/tft/matches/{puuid}/{pageNb}/{set}/{queue}")
    public ResponseEntity<List<TFTMatchEntity>> getLocalMatches(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String queue, @PathVariable String set) {
        LOGGER.info("Retrieving matches locally with PUUID : {}, queue type : {} and set : {}", puuid, queue, set);
        List<TFTMatchEntity> matches = tftMatchService.getLocalMatches(puuid, pageNb, queue, set);
        LOGGER.info("Recovered matches : {}", matches);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/tft/matches/{puuid}/{pageNb}/{set}")
    public ResponseEntity<List<TFTMatchEntity>> getLocalMatchesWithoutQueueTypeRestriction(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String set) {
        return getLocalMatches(puuid, pageNb, null, set);
    }

    @GetMapping("/tft/matches/count/{puuid}/{set}/{queue}")
    public ResponseEntity<Long> getLocalMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable String set) {
        LOGGER.info("Retrieving count of matches locally with PUUID : {}, queue type : {} and set : {}", puuid, queue, set);
        long matchesCount = tftMatchService.getLocalMatchesCount(puuid, queue, set);
        LOGGER.info("Count of matches locally : {}", matchesCount);
        return new ResponseEntity<>(matchesCount, HttpStatus.OK);
    }

    @GetMapping("/tft/matches/count/{puuid}/{set}")
    public ResponseEntity<Long> getLocalMatchesCountWithoutQueueTypeRestriction(@PathVariable String puuid, @PathVariable String set) {
        return getLocalMatchesCount(puuid, null, set);
    }

    @PostMapping("/tft/matches/{puuid}")
    public ResponseEntity<Boolean> updateRemoteToLocalMatches(@PathVariable String puuid) {
        LOGGER.info("Updating remote to local matches with PUUID : {}", puuid);
        tftMatchService.updateRemoteToLocalMatches(puuid);
        LOGGER.info("Matches updated");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}