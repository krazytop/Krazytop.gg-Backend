package com.krazytop.controller.riot;

import com.krazytop.entity.HTTPResponse;
import com.krazytop.nomenclature.lol.LOLPatchNomenclature;
import com.krazytop.nomenclature.tft.TFTPatchNomenclature;
import com.krazytop.repository.lol.LOLPatchNomenclatureRepository;
import com.krazytop.repository.tft.TFTPatchNomenclatureRepository;
import com.krazytop.service.riot.RIOTPatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class RIOTPatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTPatchController.class);

    private final RIOTPatchService patchService;

    @Autowired
    public RIOTPatchController(RIOTPatchService patchService){
        this.patchService = patchService;
    }

    @GetMapping("/riot/nomenclatures")
    public ResponseEntity<HTTPResponse> updateAllPatches() throws IOException, URISyntaxException {
        LOGGER.info("Updating all RIOT nomenclatures");
        patchService.updateAllPatches();
        return new ResponseEntity<>(new HTTPResponse("All RIOT nomenclatures are up to date", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/lol/patch/{patchId}/{language}")
    public ResponseEntity<LOLPatchNomenclature> getLOLPatch(@PathVariable String patchId, @PathVariable String language) {
        LOGGER.info("Retrieving LOL patch");
        Optional<LOLPatchNomenclature> patch = patchService.getLOLPatch(patchId, language);
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
        Optional<TFTPatchNomenclature> patch = patchService.getTFTPatch(patchId, language);
        if (patch.isPresent()) {
            LOGGER.info("TFT patch retrieved");
            return new ResponseEntity<>(patch.get(), HttpStatus.OK);
        } else {
            LOGGER.info("TFT patch not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}