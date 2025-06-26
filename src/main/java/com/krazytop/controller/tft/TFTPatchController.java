package com.krazytop.controller.tft;

import com.krazytop.api_gateway.api.generated.TftApi;
import com.krazytop.api_gateway.model.generated.TFTPatchDTO;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.mapper.tft.TFTPatchMapper;
import com.krazytop.nomenclature.tft.TFTPatch;
import com.krazytop.service.tft.TFTPatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
public class TFTPatchController implements TftApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTPatchController.class);

    private final TFTPatchService patchService;
    private final TFTPatchMapper patchMapper;

    @Autowired
    public TFTPatchController(TFTPatchService patchService, TFTPatchMapper patchMapper){
        this.patchService = patchService;
        this.patchMapper = patchMapper;
    }

    public ResponseEntity<String> updateAllPatches() {
        try {
            LOGGER.info("Updating all TFT patches");
            patchService.updateAllPatches();
            return new ResponseEntity<>("All TFT patches are up to date", HttpStatus.OK);
        } catch (IOException | URISyntaxException ex) {
            throw new CustomException(ApiErrorEnum.PATCH_UPDATE_ERROR, ex);
        }
    }

    public ResponseEntity<TFTPatchDTO> getPatch(String patchId, String language) {
        LOGGER.info("Retrieving TFT patch");
        Optional<TFTPatch> patch = patchService.getPatch(patchId, language);
        if (patch.isPresent()) {
            LOGGER.info("TFT patch retrieved");
            return new ResponseEntity<>(patchMapper.toDTO(patch.get()), HttpStatus.OK);
        } else {
            LOGGER.info("TFT patch not found");
            throw new CustomException(ApiErrorEnum.PATCH_NOT_FOUND);
        }
    }
}