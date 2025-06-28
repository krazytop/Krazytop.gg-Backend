package com.krazytop.controller.tft;

import com.krazytop.api_gateway.api.generated.TeamfightTacticsPatchApi;
import com.krazytop.api_gateway.model.generated.TFTPatchDTO;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.service.tft.TFTPatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class TFTPatchController implements TeamfightTacticsPatchApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTPatchController.class);

    private final TFTPatchService patchService;

    @Autowired
    public TFTPatchController(TFTPatchService patchService){
        this.patchService = patchService;
    }

    @Override
    public ResponseEntity<String> updateAllPatches() {
        try {
            LOGGER.info("Updating all TFT patches");
            patchService.updateAllPatches();
            return new ResponseEntity<>("All TFT patches are up to date", HttpStatus.OK);
        } catch (IOException | URISyntaxException ex) {
            throw new CustomException(ApiErrorEnum.PATCH_UPDATE_ERROR, ex);
        }
    }

    @Override
    public ResponseEntity<TFTPatchDTO> getPatch(String patchId, String language) {
        LOGGER.info("Retrieving TFT patch {} with {} language", patchId,  language);
        TFTPatchDTO patch = patchService.getPatchDTO(patchId, language);
        LOGGER.info("TFT patch retrieved");
        return new ResponseEntity<>(patch, HttpStatus.OK);
    }
}