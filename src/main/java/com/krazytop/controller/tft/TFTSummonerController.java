package com.krazytop.controller.tft;

import com.krazytop.api_gateway.model.generated.RIOTSummonerDTO;
import com.krazytop.entity.riot.RIOTSummoner;
import com.krazytop.service.tft.TFTSummonerService;
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
public class TFTSummonerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTSummonerController.class);

    private final TFTSummonerService summonerService;

    @Autowired
    public TFTSummonerController(TFTSummonerService summonerService){
        this.summonerService = summonerService;
    }

    @GetMapping("/tft/summoner/{tag}/{name}")
    public ResponseEntity<RIOTSummonerDTO> getLOLSummoner(@PathVariable String tag, @PathVariable String name) {
        LOGGER.info("Retrieving TFT summoner with name and tag");
        RIOTSummonerDTO summoner = summonerService.getSummonerDTO(tag, name);
        LOGGER.info("LOL summoner retrieved");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @GetMapping("/tft/summoner/{puuid}")
    public ResponseEntity<RIOTSummonerDTO> getLOLSummoner(@PathVariable String puuid) {
        LOGGER.info("Retrieving TFT summoner with puuid");
        RIOTSummonerDTO summoner = summonerService.getSummonerDTO(puuid);
        LOGGER.info("LOL summoner retrieved");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @PostMapping("/tft/summoner/{puuid}")
    public ResponseEntity<RIOTSummonerDTO> updateLOLSummoner(@PathVariable String puuid) {
        LOGGER.info("Updating TFT summoner");
        RIOTSummonerDTO summoner = summonerService.updateSummoner(puuid);
        LOGGER.info("TFT summoner updated");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }
}