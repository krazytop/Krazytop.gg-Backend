package com.krazytop.controller.lol;

import com.krazytop.entity.HTTPResponse;
import com.krazytop.service.lol.LOLNomenclatureService;
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
public class LOLNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureController.class);

    private final LOLNomenclatureService nomenclatureService;

    @Autowired
    public LOLNomenclatureController(LOLNomenclatureService nomenclatureService){
        this.nomenclatureService = nomenclatureService;
    }

    @GetMapping("/lol/nomenclature")
    public ResponseEntity<HTTPResponse> updateNomenclatures() throws IOException, URISyntaxException {
        LOGGER.info("Updating all TFT nomenclatures");
        this.nomenclatureService.updateAllNomenclatures();
        return new ResponseEntity<>(new HTTPResponse("All LOL nomenclatures are up to date", HttpStatus.OK.value()), HttpStatus.OK);
    }

}