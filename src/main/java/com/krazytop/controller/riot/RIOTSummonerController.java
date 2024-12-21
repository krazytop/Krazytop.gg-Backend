package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.service.riot.RIOTSummonerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RIOTSummonerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTSummonerController.class);

    private final RIOTSummonerService riotSummonerService;

    @Autowired
    public RIOTSummonerController(RIOTSummonerService riotSummonerService){
        this.riotSummonerService = riotSummonerService;
    }

    @GetMapping("/riot/summoner/local/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Retrieving RIOT local summoner");
        try {
            Optional<RIOTSummonerEntity> summoner = riotSummonerService.getLocalSummoner(region, tag, name);
            if (summoner.isPresent()) {
                LOGGER.info("RIOT local summoner retrieved");
                return new ResponseEntity<>(summoner.get(), HttpStatus.OK);
            } else {
                LOGGER.info("RIOT local summoner not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving RIOT local summoner : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/riot/summoner/remote/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getRemoteSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Retrieving RIOT remote summoner");
        try {
            RIOTSummonerEntity summoner = riotSummonerService.getRemoteSummonerByNameAndTag(region, tag, name);
            if (summoner != null) {
                LOGGER.info("RIOT remote summoner successfully retrieved");
                return new ResponseEntity<>(summoner, HttpStatus.OK);
            } else {
                LOGGER.info("RIOT remote summoner not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving RIOT remote summoner : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/riot/summoner/update/{region}/{tag}/{name}")//TODO enleverupdate
    public ResponseEntity<String> updateRemoteToLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Updating RIOT summoner");//TODO si le tag & name change la page ne sera plus la bonne => retourner le summoner
        try {
            riotSummonerService.updateRemoteToLocalSummoner(region, tag, name);
            LOGGER.info("RIOT summoner successfully updated");
            return new ResponseEntity<>("RIOT summoner successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating RIOT summoner : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating RIOT summoner", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}