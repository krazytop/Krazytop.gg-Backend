package com.krazytop.controller.lol;

import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.service.lol.LOLRankService;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class LOLRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLRankController.class);

    private final LOLRankService lolRankService;
    private final RIOTRankService riotRankService;

    @Autowired
    public LOLRankController(LOLRankService lolRankService, RIOTRankService riotRankService) {
        this.lolRankService = lolRankService;
        this.riotRankService = riotRankService;
    }

    @GetMapping("/lol/ranks/{puuid}")
    public ResponseEntity<RIOTRankEntity> getRank(@PathVariable String puuid) {
        LOGGER.info("Retrieving LOL local rank");
        Optional<RIOTRankEntity> rank = riotRankService.getRanks(puuid, GameEnum.LOL);
        if (rank.isPresent()) {
            LOGGER.info("LOL local rank retrieved");
            return new ResponseEntity<>(rank.get(), HttpStatus.OK);
        } else {
            LOGGER.info("LOL local rank not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/lol/ranks/{puuid}")
    public ResponseEntity<String> updateRank(@PathVariable String puuid) throws URISyntaxException, IOException {
        LOGGER.info("Updating LOL ranks");
        lolRankService.updateRanks(puuid);
        LOGGER.info("LOL ranks successfully updated");
        return new ResponseEntity<>("LOL ranks successfully updated", HttpStatus.OK);
    }
}