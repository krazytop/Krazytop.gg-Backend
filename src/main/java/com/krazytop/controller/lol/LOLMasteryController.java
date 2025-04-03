package com.krazytop.controller.lol;

import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.lol.LOLMasteriesEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.service.lol.LOLMasteryService;
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
public class LOLMasteryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMasteryController.class);

    private final LOLMasteryService masteryService;

    @Autowired
    public LOLMasteryController(LOLMasteryService masteryService) {
        this.masteryService = masteryService;
    }

    @GetMapping("/lol/masteries/{puuid}")
    public ResponseEntity<LOLMasteriesEntity> getMasteries(@PathVariable String puuid) {
        LOGGER.info("Retrieving LOL masteries");
        return new ResponseEntity<>(masteryService.getMasteries(puuid)
                .orElseThrow(() -> new CustomHTTPException(RIOTHTTPErrorResponsesEnum.MASTERIES_NOT_FOUND)), HttpStatus.OK);
    }

    @PostMapping("/lol/masteries/{region}/{puuid}")
    public ResponseEntity<String> updateMasteries(@PathVariable String region, @PathVariable String puuid) {
        LOGGER.info("Updating LOL masteries");
        masteryService.updateMasteries(region, puuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}