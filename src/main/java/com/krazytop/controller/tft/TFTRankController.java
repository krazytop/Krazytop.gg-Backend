package com.krazytop.controller.tft;

import com.krazytop.entity.riot.RIOTRankEntity;
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

@RestController
public class TFTRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTRankController.class);

    private final TFTRankService rankService;

    @Autowired
    public TFTRankController(TFTRankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping("/tft/rank/{puuid}")
    public ResponseEntity<RIOTRankEntity> getLocalRank(@PathVariable String puuid) {
        LOGGER.info("Retrieving TFT local rank");
        try {
            RIOTRankEntity rank = rankService.getLocalRank(puuid);
            if (rank != null) {
                LOGGER.info("TFT local rank retrieved");
                return new ResponseEntity<>(rank, HttpStatus.OK);
            } else {
                LOGGER.info("TFT local rank not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving TFT local rank : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/tft/rank/{summonerId}")
    public ResponseEntity<String> updateRemoteToLocalRank(@PathVariable String summonerId) {
        LOGGER.info("Updating TFT ranks");
        try {
            rankService.updateRemoteToLocalRank(summonerId);
            LOGGER.info("TFT ranks successfully updated");
            return new ResponseEntity<>("TFT ranks successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating TFT ranks : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating TFT ranks", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}