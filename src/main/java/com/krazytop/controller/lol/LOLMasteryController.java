package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LeagueOfLegendsMasteryApi;
import com.krazytop.api_gateway.model.generated.LOLMasteriesDTO;
import com.krazytop.service.lol.LOLMasteryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LOLMasteryController implements LeagueOfLegendsMasteryApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMasteryController.class);

    private final LOLMasteryService masteryService;

    @Autowired
    public LOLMasteryController(LOLMasteryService masteryService) {
        this.masteryService = masteryService;
    }

    @Override
    public ResponseEntity<LOLMasteriesDTO> getMasteries(String puuid) {
        LOGGER.info("Retrieving LOL masteries");
        LOLMasteriesDTO masteriesDTO = masteryService.getMasteriesDTO(puuid);
        LOGGER.info("LOL masteries retrieved");
        return new ResponseEntity<>(masteriesDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateMasteries(String puuid) {
        LOGGER.info("Updating LOL masteries");
        masteryService.updateMasteries(puuid);
        LOGGER.info("LOL masteries updated");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}