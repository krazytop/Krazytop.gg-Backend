package com.krazytop.controller.tft;

import com.krazytop.entity.tft.TFTRankEntity;
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

import java.util.List;

@RestController
public class TFTRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTRankController.class);

    private final TFTRankService tftRankService;

    @Autowired
    public TFTRankController(TFTRankService tftRankService) {
        this.tftRankService = tftRankService;
    }

    @GetMapping("/tft/rank/{summonerId}/{queueType}")
    public ResponseEntity<TFTRankEntity> getLocalRank(@PathVariable String summonerId, @PathVariable String queueType) {
        LOGGER.info("Retrieving local rank");
        TFTRankEntity rank = tftRankService.getLocalRank(summonerId, queueType);
        LOGGER.info("Recovered rank");
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @PostMapping("/tft/rank/{summonerId}")
    public ResponseEntity<List<TFTRankEntity>> updateRemoteToLocalRank(@PathVariable String summonerId) {
        LOGGER.info("Updating ranks");
        List<TFTRankEntity> rank = tftRankService.updateRemoteToLocalRank(summonerId);
        LOGGER.info("Ranks updated");
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }
}