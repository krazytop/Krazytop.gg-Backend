package com.krazytop.controller.tft;

import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.service.riot.RIOTRankService;
import com.krazytop.service.tft.TFTRankService;
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
public class TFTRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTRankController.class);

    private final TFTRankService tftRankService;
    private final RIOTRankService riotRankService;

    @Autowired
    public TFTRankController(TFTRankService tftRankService, RIOTRankService riotRankService) {
        this.tftRankService = tftRankService;
        this.riotRankService = riotRankService;
    }

    @GetMapping("/tft/ranks/{puuid}")
    public ResponseEntity<RIOTRankEntity> getRanks(@PathVariable String puuid) {
        LOGGER.info("Retrieving TFT local rank");
        Optional<RIOTRankEntity> rank = riotRankService.getRanks(puuid, GameEnum.TFT);
        if (rank.isPresent()) {
            LOGGER.info("TFT local rank retrieved");
            return new ResponseEntity<>(rank.get(), HttpStatus.OK);
        } else {
            LOGGER.info("TFT local rank not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tft/ranks/{puuid}")
    public ResponseEntity<String> updateRanks(@PathVariable String puuid) throws URISyntaxException, IOException {
        LOGGER.info("Updating TFT ranks");
        tftRankService.updateRanks(puuid);
        LOGGER.info("TFT ranks successfully updated");
        return new ResponseEntity<>("TFT ranks successfully updated", HttpStatus.OK);
    }
}