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
        LOGGER.info("Retrieving of local information from summoner : {} #{} on region : {}", name, tag, region);
        RIOTSummonerEntity summoner = riotSummonerService.getLocalSummoner(region, tag, name);
        LOGGER.info("Recovered summoner : {}", summoner);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @GetMapping("/riot/summoner/remote/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getRemoteSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Retrieving of remote information from summoner : {} #{} on region : {}", name, tag, region);
        RIOTSummonerEntity summoner = riotSummonerService.getRemoteSummoner(region, tag, name);
        LOGGER.info("Recovered summoner : {}", summoner);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @PostMapping("/riot/summoner/update/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> updateRemoteToLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Updating remote to local summoner : {} #{} on region : {}", name, tag, region);
        RIOTSummonerEntity summoner = riotSummonerService.updateRemoteToLocalSummoner(region, tag, name);
        LOGGER.info("Updated summoner : {}", summoner);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }
}