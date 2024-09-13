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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class LOLMatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchController.class);

    @Autowired
    private LOLMatchService lolMatchService;

    @GetMapping("/lol/matches/{puuid}/{pageNb}/{queue}/{role}")
    public ResponseEntity<List<LOLMatchEntity>> getLocalMatches(@PathVariable String puuid, @PathVariable int pageNb, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving matches locally with PUUID : {}, queue type : {} and role : {}", puuid, queue, role);
        List<LOLMatchEntity> matches = lolMatchService.getLocalMatches(puuid, pageNb, queue, role);
        LOGGER.info("Recovered matches : {}", matches);
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/lol/matches/count/{puuid}/{queue}/{role}")
    public ResponseEntity<Long> getLocalMatchesCount(@PathVariable String puuid, @PathVariable String queue, @PathVariable String role) {
        LOGGER.info("Retrieving count of matches locally with PUUID : {}, queue type : {} and role : {}", puuid, queue, role);
        long matchesCount = lolMatchService.getLocalMatchesCount(puuid, queue, role);
        LOGGER.info("Count of matches locally : {}", matchesCount);
        return new ResponseEntity<>(matchesCount, HttpStatus.OK);
    }

    @PostMapping("/lol/matches/{puuid}")
    public ResponseEntity<Boolean> updateRemoteToLocalMatches(@PathVariable String puuid) {
        LOGGER.info("Updating remote to local matches with PUUID : {}", puuid);
        //lolMatchService.updateRemoteToLocalMatches(puuid);
        try {
            lolMatchService.updateMatchTEST("EUW1_7116043000");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Matches updated");
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}