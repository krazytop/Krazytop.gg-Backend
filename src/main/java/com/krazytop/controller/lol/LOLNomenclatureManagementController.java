package com.krazytop.controller.lol;

import com.krazytop.nomenclature_management.LOLNomenclatureManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LOLNomenclatureManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureManagementController.class);

    @Autowired
    private LOLNomenclatureManagement lolNomenclatureManagement;

    @PostMapping("/lol/nomenclature/queue")
    public ResponseEntity<Boolean> updateQueueNomenclature() {
        LOGGER.info("Updating lol queue nomenclature");
        boolean success = lolNomenclatureManagement.updateQueueNomenclature();
        LOGGER.info("LOL queue nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/lol/nomenclature/champion")
    public ResponseEntity<Boolean> updateChampionNomenclature() {
        LOGGER.info("Updating lol champion nomenclature");
        boolean success = lolNomenclatureManagement.updateChampionNomenclature();
        LOGGER.info("LOL champion nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/lol/nomenclature/item")
    public ResponseEntity<Boolean> updateItemNomenclature() {
        LOGGER.info("Updating lol item nomenclature");
        boolean success = lolNomenclatureManagement.updateItemNomenclature();
        LOGGER.info("LOL item nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/lol/nomenclature/summoner-spell")
    public ResponseEntity<Boolean> updateSummonerSpellNomenclature() {
        LOGGER.info("Updating lol summoner spell nomenclature");
        boolean success = lolNomenclatureManagement.updateSummonerSpellNomenclature();
        LOGGER.info("LOL summoner spell nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/lol/nomenclature/rune")
    public ResponseEntity<Boolean> updateRuneNomenclature() {
        LOGGER.info("Updating lol rune nomenclature");
        boolean success = lolNomenclatureManagement.updateRuneNomenclature();
        LOGGER.info("LOL rune nomenclature updated");
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("/lol/nomenclature")
    public ResponseEntity<Boolean> addAllNomenclature() {
        LOGGER.info("Updating all lol nomenclature");
        boolean successQueue = Boolean.TRUE.equals(this.updateQueueNomenclature().getBody());
        boolean successChampion = Boolean.TRUE.equals(this.updateChampionNomenclature().getBody());
        boolean successItem = Boolean.TRUE.equals(this.updateItemNomenclature().getBody());
        boolean successSummonerSpell = Boolean.TRUE.equals(this.updateSummonerSpellNomenclature().getBody());
        boolean successRune = Boolean.TRUE.equals(this.updateRuneNomenclature().getBody());
        LOGGER.info("All lol nomenclature updated");
        return new ResponseEntity<>(successQueue && successChampion && successItem && successSummonerSpell && successRune, HttpStatus.OK);
    }

}