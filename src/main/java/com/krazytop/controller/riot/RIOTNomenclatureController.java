package com.krazytop.controller.riot;

import com.krazytop.entity.HTTPResponse;
import com.krazytop.service.riot.RIOTNomenclatureService;
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
public class RIOTNomenclatureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTNomenclatureController.class);

    private final RIOTNomenclatureService nomenclatureService;

    @Autowired
    public RIOTNomenclatureController(RIOTNomenclatureService nomenclatureService) {
        this.nomenclatureService = nomenclatureService;
    }

    @GetMapping("/riot/nomenclatures")
    public ResponseEntity<HTTPResponse> updateAllNomenclatures() throws IOException, URISyntaxException {
        LOGGER.info("Updating all RIOT nomenclatures");
        nomenclatureService.updateAllNomenclatures();
        return new ResponseEntity<>(new HTTPResponse("All RIOT nomenclatures are up to date", HttpStatus.OK), HttpStatus.OK);
    }

}