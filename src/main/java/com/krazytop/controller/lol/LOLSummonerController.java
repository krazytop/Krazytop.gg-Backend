package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LeagueOfLegendsSummonerApi;
import com.krazytop.api_gateway.model.generated.RIOTSummonerDTO;
import com.krazytop.service.lol.LOLSummonerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LOLSummonerController implements LeagueOfLegendsSummonerApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLSummonerController.class);

    private final LOLSummonerService summonerService;

    @Autowired
    public LOLSummonerController(LOLSummonerService summonerService){
        this.summonerService = summonerService;
    }

    @Override
    public ResponseEntity<RIOTSummonerDTO> getSummonerByTagAndName(String tag, String name) {
        LOGGER.info("Retrieving LOL summoner with name and tag");
        RIOTSummonerDTO summoner = summonerService.getSummonerDTO(tag, name);
        LOGGER.info("LOL summoner retrieved");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RIOTSummonerDTO> getSummonerByPuuid(String puuid) {
        LOGGER.info("Retrieving LOL summoner with puuid");
        RIOTSummonerDTO summoner = summonerService.getSummonerDTO(puuid);
        LOGGER.info("LOL summoner retrieved");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RIOTSummonerDTO> updateSummoner(String puuid) {
        LOGGER.info("Updating LOL summoner");
        RIOTSummonerDTO summoner = summonerService.updateSummoner(puuid);
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }
}