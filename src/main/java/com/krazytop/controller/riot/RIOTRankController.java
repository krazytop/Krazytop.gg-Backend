package com.krazytop.controller.riot;

import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.service.riot.RIOTRankService;
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
public class RIOTRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTRankController.class);

    private final RIOTRankService rankService;

    @Autowired
    public RIOTRankController(RIOTRankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping("/lol/ranks/{puuid}")
    public ResponseEntity<RIOTRankEntity> getLOLRanks(@PathVariable String puuid) {
        return getRanks(puuid, GameEnum.LOL);
    }

    @GetMapping("/tft/ranks/{puuid}")
    public ResponseEntity<RIOTRankEntity> getTFTRanks(@PathVariable String puuid) {
        return getRanks(puuid, GameEnum.TFT);
    }

    private ResponseEntity<RIOTRankEntity> getRanks(String puuid, GameEnum game) {
        LOGGER.info("Retrieving {} ranks", game);
        return new ResponseEntity<>(rankService.getRanks(puuid, game)
                .orElseThrow(() -> new CustomHTTPException(RIOTHTTPErrorResponsesEnum.RANKS_NOT_FOUND)), HttpStatus.OK);
    }

    @PostMapping("/lol/ranks/{region}/{puuid}")
    public ResponseEntity<Void> updateLOLRanks(@PathVariable String region, @PathVariable String puuid) {
        return updateRanks(region, puuid, GameEnum.LOL);
    }

    @PostMapping("/tft/ranks/{region}/{puuid}")
    public ResponseEntity<Void> updateTFTRanks(@PathVariable String region, @PathVariable String puuid) {
        return updateRanks(region, puuid, GameEnum.TFT);
    }

    private ResponseEntity<Void> updateRanks(String region, String puuid, GameEnum game) {
        LOGGER.info("Updating {} ranks", game);
        rankService.updateRanks(region, puuid, game);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}