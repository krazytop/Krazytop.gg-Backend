package com.krazytop.controller.tft;

import com.krazytop.service.tft.TFTNomenclatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class TFTNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTNomenclatureController.class);

    private final TFTNomenclatureService nomenclatureService;

    @Autowired
    public TFTNomenclatureController(TFTNomenclatureService nomenclatureService) {
        this.nomenclatureService = nomenclatureService;
    }

    @GetMapping("/tft/nomenclature")
    public ResponseEntity<String> updateNomenclatures(@RequestParam(required = false, defaultValue = "false") boolean legacy) {
        LOGGER.info("Updating all TFT nomenclatures");
        try {
            if (legacy) {
            }
            if (this.nomenclatureService.updateAllNomenclatures()) {
                return new ResponseEntity<>("All TFT nomenclatures are successfully updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("All TFT nomenclatures are already up to date", HttpStatus.OK);
            }
        } catch (Exception e) {
            LOGGER.error("Error while updating all TFT nomenclatures : {}", e.getMessage());
            return new ResponseEntity<>("Error while updating all TFT nomenclatures", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/tft/nomenclature/test")
    public ResponseEntity<String> test() throws IOException, URISyntaxException {
        this.nomenclatureService.updateAllNomenclatures();
        return null;
    }

}