package com.krazytop.controller.tft;

import com.krazytop.entity.HTTPResponse;
import com.krazytop.service.tft.TFTNomenclatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<HTTPResponse> updateNomenclatures() throws IOException, URISyntaxException {
        LOGGER.info("Updating all TFT nomenclatures");
        this.nomenclatureService.updateAllNomenclatures();
        return new ResponseEntity<>(new HTTPResponse("All TFT nomenclatures are up to date", HttpStatus.OK.value()), HttpStatus.OK);
    }

}