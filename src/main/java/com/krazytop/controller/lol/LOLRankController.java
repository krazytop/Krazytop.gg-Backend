package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LeagueOfLegendsRankApi;
import com.krazytop.api_gateway.model.generated.RIOTRankDTO;
import com.krazytop.service.lol.LOLRankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LOLRankController implements LeagueOfLegendsRankApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLRankController.class);

    private final LOLRankService rankService;

    @Autowired
    public LOLRankController(LOLRankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public ResponseEntity<RIOTRankDTO> getRanks(String puuid) {
        LOGGER.info("Retrieving LOL ranks");
        RIOTRankDTO rank = rankService.getRanksDTO(puuid);
        LOGGER.info("LOL ranks retrieved");
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateRanks(String puuid) {
        LOGGER.info("Updating LOL ranks");
        rankService.updateRanks(puuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}