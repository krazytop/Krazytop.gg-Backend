package com.krazytop.controller.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.service.riot.RIOTSummonerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RIOTSummonerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTSummonerController.class);

    private final RIOTSummonerService riotSummonerService;

    @Autowired
    public RIOTSummonerController(RIOTSummonerService riotSummonerService){
        this.riotSummonerService = riotSummonerService;
    }

    @GetMapping("/lol/summoner/local/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getLOLLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return getLocalSummoner(region, tag, name, GameEnum.LOL);
    }

    @GetMapping("/tft/summoner/local/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getTFTLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return getLocalSummoner(region, tag, name, GameEnum.TFT);
    }

    private ResponseEntity<RIOTSummonerEntity> getLocalSummoner(String region, String tag, String name, GameEnum game) {
        LOGGER.info("Retrieving local summoner");
        try {
            Optional<RIOTSummonerEntity> summoner = riotSummonerService.getLocalSummoner(region, tag, name, game);
            if (summoner.isPresent()) {
                LOGGER.info("Local summoner retrieved");
                return new ResponseEntity<>(summoner.get(), HttpStatus.OK);
            } else {
                LOGGER.info("Local summoner not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving local summoner : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/lol/summoner/remote/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getLOLRemoteSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return getRemoteSummoner(region, tag, name, GameEnum.LOL);
    }

    @GetMapping("/tft/summoner/remote/{region}/{tag}/{name}")
    public ResponseEntity<RIOTSummonerEntity> getTFTRemoteSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return getRemoteSummoner(region, tag, name, GameEnum.TFT);
    }

    public ResponseEntity<RIOTSummonerEntity> getRemoteSummoner(String region, String tag, String name, GameEnum game) {
        LOGGER.info("Retrieving remote summoner");
        try {
            RIOTSummonerEntity summoner = riotSummonerService.getRemoteSummonerByNameAndTag(region, tag, name, game);
            if (summoner != null) {
                LOGGER.info("Remote summoner successfully retrieved");
                return new ResponseEntity<>(summoner, HttpStatus.OK);
            } else {
                LOGGER.info("Remote summoner not found");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while retrieving remote summoner : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/lol/summoner/update/{region}/{tag}/{name}")//TODO enleverupdate
    public ResponseEntity<String> updateLOLRemoteToLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return updateRemoteToLocalSummoner(region, tag, name, GameEnum.LOL);
    }

    @PostMapping("/tft/summoner/update/{region}/{tag}/{name}")//TODO enleverupdate
    public ResponseEntity<String> updateTFTRemoteToLocalSummoner(@PathVariable String region, @PathVariable String tag, @PathVariable String name) {
        return updateRemoteToLocalSummoner(region, tag, name, GameEnum.TFT);
    }

    public ResponseEntity<String> updateRemoteToLocalSummoner(String region, String tag, String name, GameEnum game) {
        LOGGER.info("Updating summoner");//TODO si le tag & name change la page ne sera plus la bonne => retourner le summoner
        try {
            riotSummonerService.updateRemoteToLocalSummoner(region, tag, name, game);
            LOGGER.info("Summoner successfully updated");
            return new ResponseEntity<>("Summoner successfully updated", HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating summoner : {}", e.getMessage());
            return new ResponseEntity<>("An error occurred while updating summoner", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}