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

import java.util.List;

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
        LOGGER.info("Retrieving rank locally with summoner ID : {} and queue type : {}", summonerId, queueType);
        LOLRankEntity rank = lolRankService.getLocalRank(summonerId, queueType);
        LOGGER.info("Recovered rank : {} for queue type : {}", rank, queueType);
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @PostMapping("/lol/rank/{summonerId}")
    public ResponseEntity<List<LOLRankEntity>> updateRemoteToLocalRank(@PathVariable String summonerId) {
        LOGGER.info("Updating remote to local rank with summoner ID : {}", summonerId);
        List<LOLRankEntity> rank = lolRankService.updateRemoteToLocalRank(summonerId);
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }
}