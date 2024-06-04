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

    @Autowired
    private TFTRankService tftRankService;

    @GetMapping("/tft/rank/{summonerId}/{queueType}")
    public ResponseEntity<TFTRankEntity> getLocalRank(@PathVariable String summonerId, @PathVariable String queueType) {
        LOGGER.info("Retrieving rank locally with summoner ID : {} and queue type : {}", summonerId, queueType);
        TFTRankEntity rank = tftRankService.getLocalRank(summonerId, queueType);
        LOGGER.info("Recovered rank : {} for queue type : {}", rank, queueType);
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @PostMapping("/tft/rank/{summonerId}")
    public ResponseEntity<List<TFTRankEntity>> updateRemoteToLocalRank(@PathVariable String summonerId) {
        LOGGER.info("Updating remote to local rank with summoner ID : {}", summonerId);
        List<TFTRankEntity> rank = tftRankService.updateRemoteToLocalRank(summonerId);
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }
}