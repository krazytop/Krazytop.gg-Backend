package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
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

    @GetMapping("/lol/summoner/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getLOLSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return getSummoner(region, tag, name, GameEnum.LOL);
    }

    @GetMapping("/tft/summoner/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getTFTSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return getSummoner(region, tag, name, GameEnum.TFT);
    }

    private ResponseEntity<RIOTSummonerEntity> getSummoner(String region, String tag, String name, GameEnum game) {
        LOGGER.info("Retrieving {} summoner with name and tag", game);
        RIOTSummonerEntity summoner = riotSummonerService.getSummoner(region, tag, name, game);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @GetMapping("/lol/summoner/{region}/{summonerId}")
    public ResponseEntity<RIOTSummonerEntity> getLOLSummoner(@PathVariable String region, @PathVariable String summonerId) {
        return getSummoner(region, summonerId, GameEnum.LOL);
    }

    @GetMapping("/tft/summoner/{region}/{summonerId}")
    public ResponseEntity<RIOTSummonerEntity> getTFTSummoner(@PathVariable String region, @PathVariable String summonerId) {
        return getSummoner(region, summonerId, GameEnum.TFT);
    }

    private ResponseEntity<RIOTSummonerEntity> getSummoner(String region, String summonerId, GameEnum game) {
        LOGGER.info("Retrieving {} summoner with id", game);
        RIOTSummonerEntity summoner = riotSummonerService.getSummoner(region, summonerId, game);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @PostMapping("/lol/summoner/{region}/{summonerId}")
    public ResponseEntity<RIOTSummonerEntity> updateLOLSummoner(@PathVariable String region, @PathVariable String summonerId) {
        return updateSummoner(region, summonerId, GameEnum.LOL);
    }

    @PostMapping("/tft/summoner/{region}/{summonerId}")
    public ResponseEntity<RIOTSummonerEntity> updateTFTSummoner(@PathVariable String region, @PathVariable String summonerId) {
        return updateSummoner(region, summonerId, GameEnum.TFT);
    }

    private ResponseEntity<RIOTSummonerEntity> updateSummoner(String region, String summonerId, GameEnum game) {
        LOGGER.info("Updating {} summoner", game);
        RIOTSummonerEntity summoner = riotSummonerService.updateSummoner(region, summonerId, game);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }
}