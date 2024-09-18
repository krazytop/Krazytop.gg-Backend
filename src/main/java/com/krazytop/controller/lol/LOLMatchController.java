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

    private final LOLMatchService lolMatchService;

    @Autowired
    public LOLMatchController(LOLMatchService lolMatchService){
        this.lolMatchService = lolMatchService;
    }

    @GetMapping("/lol/matches/{puuid}/{pageNb}/{queue}/{role}")
    public ResponseEntity<List<LOLMatchEntity>> getLocalMatches(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving local matches");
        List<LOLMatchEntity> matches = lolMatchService.getLocalMatches(puuid, pageNb, queue, role);
        LOGGER.info("Recovered matches");
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/lol/matches/count/{puuid}/{queue}/{role}")
    public ResponseEntity<Long> getLocalMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving local matches count");
        Long matchesCount = lolMatchService.getLocalMatchesCount(puuid, queue, role);
        LOGGER.info("Recovered matches count");
        return new ResponseEntity<>(matchesCount, HttpStatus.OK);
    }

    @PostMapping("/lol/matches/{puuid}")
    public ResponseEntity<Boolean> updateRemoteToLocalMatches(@PathVariable String puuid) {
        LOGGER.info("Updating matches");
        lolMatchService.updateRemoteToLocalMatches(puuid);
        LOGGER.info("Matches updated");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}