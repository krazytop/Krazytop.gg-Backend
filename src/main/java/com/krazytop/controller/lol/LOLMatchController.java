package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LeagueOfLegendsMatchApi;
import com.krazytop.api_gateway.model.generated.LOLMatchDTO;
import com.krazytop.service.lol.LOLMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LOLMatchController implements LeagueOfLegendsMatchApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchController.class);

    private final LOLMatchService matchService;

    @Autowired
    public LOLMatchController(LOLMatchService matchService){
        this.matchService = matchService;
    }

    @Override
    public ResponseEntity<List<LOLMatchDTO>> getMatches(String puuid, Integer pageNb, String queue, String role) {
        LOGGER.info("Retrieving LOL matches");
        return new ResponseEntity<>(matchService.getMatches(puuid, pageNb, queue, role), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getMatchesCount(String puuid, String queue, String role) {
        LOGGER.info("Retrieving LOL matches count");
        return new ResponseEntity<>(matchService.getMatchesCount(puuid, queue, role), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateMatches(String puuid) {
        LOGGER.info("Updating LOL matches");
        matchService.updateMatches(puuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}