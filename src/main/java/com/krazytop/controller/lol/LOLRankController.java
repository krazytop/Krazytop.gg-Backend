package com.krazytop.controller.lol;

import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.service.lol.LOLRankService;
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
public class LOLRankController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLRankController.class);

    private final LOLRankService lolRankService;

    @Autowired
    public LOLRankController(LOLRankService lolRankService) {
        this.lolRankService = lolRankService;
    }

    @GetMapping("/lol/rank/{summonerId}/{queueType}")
    public ResponseEntity<LOLRankEntity> getLocalRank(@PathVariable String summonerId, @PathVariable String queueType) {
        LOGGER.info("Retrieving LOL local rank");
        try {
            LOLRankEntity rank = lolRankService.getLocalRank(summonerId, queueType);
            if (rank != null) {
                LOGGER.info("LOL local rank retrieved");
                return new ResponseEntity<>(rank, HttpStatus.OK);
            } else {
                LOGGER.info("LOL local rank not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving LOL local rank : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/lol/rank/{summonerId}")
    public ResponseEntity<String> updateRemoteToLocalRank(@PathVariable String summonerId) {
        LOGGER.info("Updating LOL ranks");
        try {
            lolRankService.updateRemoteToLocalRank(summonerId);
            LOGGER.info("LOL ranks successfully updated");
            return new ResponseEntity<>("LOL ranks successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating LOL ranks : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating LOL ranks", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}