package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.service.lol.LOLMatchService;
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
        List<LOLMatchEntity> matches = matchService.getLocalMatches(puuid, pageNb, queue, role);
        LOGGER.info("LOL local matches successfully retrieved");
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/lol/matches/count/{puuid}/{queue}/{role}")
    public ResponseEntity<Long> getLocalMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving LOL local matches count");
        Long matchesCount = matchService.getLocalMatchesCount(puuid, queue, role);
        LOGGER.info("LOL local matches count successfully retrieved");
        return new ResponseEntity<>(matchesCount, HttpStatus.OK);
    }

    @PostMapping("/lol/matches/{puuid}")
    public ResponseEntity<String> updateRemoteToLocalMatches(@PathVariable String puuid) throws IOException, URISyntaxException, InterruptedException {
        LOGGER.info("Updating LOL matches");
        matchService.updateRecentMatches(puuid);
        LOGGER.info("LOL matches successfully updated");
        return new ResponseEntity<>("LOL matches successfully updated", HttpStatus.OK);
    }

}