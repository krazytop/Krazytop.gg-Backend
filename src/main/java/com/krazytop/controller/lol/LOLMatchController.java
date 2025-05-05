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

    @GetMapping("/lol/matches/{summonerId}/{pageNb}/{queue}/{role}")
    public ResponseEntity<List<LOLMatchEntity>> getMatches(@PathVariable String summonerId, @PathVariable int pageNb, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving LOL matches");
        return new ResponseEntity<>(matchService.getMatches(summonerId, pageNb, queue, role), HttpStatus.OK);
    }

    @GetMapping("/lol/matches/count/{summonerId}/{queue}/{role}")
    public ResponseEntity<Long> getMatchesCount(@PathVariable String summonerId, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving LOL matches count");
        return new ResponseEntity<>(matchService.getMatchesCount(summonerId, queue, role), HttpStatus.OK);
    }

    @PostMapping("/lol/matches/{region}/{puuid}")
    public ResponseEntity<Void> updateMatches(@PathVariable String region, @PathVariable String puuid) {
        LOGGER.info("Updating LOL matches");
        matchService.updateMatches(region, puuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}