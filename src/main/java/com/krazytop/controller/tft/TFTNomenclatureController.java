package com.krazytop.controller.tft;

import com.krazytop.service.tft.TFTNomenclatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TFTNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTNomenclatureController.class);

    private final TFTNomenclatureService tftNomenclatureService;

    @Autowired
    public TFTNomenclatureController(TFTNomenclatureService tftNomenclatureService) {
        this.tftNomenclatureService = tftNomenclatureService;
    }

    @GetMapping("/tft/nomenclature")
    public ResponseEntity<String> updateNomenclatures() {
        LOGGER.info("Updating all TFT nomenclatures");
        try {
            if (this.tftNomenclatureService.updateAllNomenclatures()) {
                return new ResponseEntity<>("All TFT nomenclatures are successfully updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("All TFT nomenclatures are already up to date", HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error("Error while updating all TFT nomenclatures : {}", e.getMessage());
            return new ResponseEntity<>("Error while updating all TFT nomenclatures", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}