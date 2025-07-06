package com.krazytop.controller.tft;

import com.krazytop.api_gateway.api.generated.TeamfightTacticsMatchApi;
import com.krazytop.api_gateway.model.generated.TFTMatchDTO;
import com.krazytop.entity.tft.TFTMatch;
import com.krazytop.service.tft.TFTMatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TFTMatchController implements TeamfightTacticsMatchApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTMatchController.class);

    private final TFTMatchService matchService;

    @Autowired
    public TFTMatchController(TFTMatchService matchService) {
        this.matchService = matchService;
    }

    @Override
    public ResponseEntity<List<TFTMatchDTO>> getMatches(String puuid, Integer pageNb, String queue, Integer set) {
        LOGGER.info("Retrieving TFT matches");
        return new ResponseEntity<>(matchService.getMatches(puuid, pageNb, queue, set), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getMatchesCount(String puuid, String queue, Integer set) {
        LOGGER.info("Retrieving TFT matches count");
        return new ResponseEntity<>(matchService.getMatchesCount(puuid, queue, set), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateMatches(String puuid) {
        LOGGER.info("Updating TFT matches");
        matchService.updateMatches(puuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}