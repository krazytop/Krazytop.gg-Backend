package com.krazytop.controller.riot;

import com.krazytop.nomenclature.lol.LOLPatchNomenclature;
import com.krazytop.nomenclature.tft.TFTPatchNomenclature;
import com.krazytop.repository.lol.LOLPatchNomenclatureRepository;
import com.krazytop.repository.tft.TFTPatchNomenclatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RIOTPatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTPatchController.class);

    private final LOLPatchNomenclatureRepository lolPatchNomenclatureRepository;
    private final TFTPatchNomenclatureRepository tftPatchNomenclatureRepository;

    @Autowired
    public RIOTPatchController(LOLPatchNomenclatureRepository lolPatchNomenclatureRepository, TFTPatchNomenclatureRepository tftPatchNomenclatureRepository){
        this.lolPatchNomenclatureRepository = lolPatchNomenclatureRepository;
        this.tftPatchNomenclatureRepository = tftPatchNomenclatureRepository;
    }

    @GetMapping("/lol/patch/{patchId}/{language}")
    public ResponseEntity<LOLPatchNomenclature> getLOLPatch(@PathVariable String patchId, @PathVariable String language) {
        LOGGER.info("Retrieving LOL patch");
        Optional<LOLPatchNomenclature> patch = lolPatchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
        if (patch.isPresent()) {
            LOGGER.info("LOL patch retrieved");
            return new ResponseEntity<>(patch.get(), HttpStatus.OK);
        } else {
            LOGGER.info("LOL patch not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tft/patch/{patchId}/{language}")
    public ResponseEntity<TFTPatchNomenclature> getTFTPatch(@PathVariable String patchId, @PathVariable String language) {
        LOGGER.info("Retrieving TFT patch");
        Optional<TFTPatchNomenclature> patch = tftPatchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
        if (patch.isPresent()) {
            LOGGER.info("TFT patch retrieved");
            return new ResponseEntity<>(patch.get(), HttpStatus.OK);
        } else {
            LOGGER.info("TFT patch not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}