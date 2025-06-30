package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LeagueOfLegendsPatchApi;
import com.krazytop.api_gateway.model.generated.LOLPatchDTO;
import com.krazytop.exception.CustomException;
import com.krazytop.exception.ApiErrorEnum;
import com.krazytop.service.lol.LOLPatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class LOLPatchController implements LeagueOfLegendsPatchApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLPatchController.class);

    private final LOLPatchService patchService;

    @Autowired
    public LOLPatchController(LOLPatchService patchService){
        this.patchService = patchService;
    }

    @Override
    public ResponseEntity<String> updateAllPatches() {
        try {
            LOGGER.info("Updating all LOL patches");
            patchService.updateAllPatches();
            return new ResponseEntity<>("All LOL patches are up to date", HttpStatus.OK);
        } catch (IOException | URISyntaxException ex) {
            throw new CustomException(ApiErrorEnum.PATCH_UPDATE_ERROR, ex);
        }
    }

    @Override
    public ResponseEntity<LOLPatchDTO> getPatch(String patchId, String language) {
        LOGGER.info("Retrieving LOL patch {} with {} language", patchId,  language);
        LOLPatchDTO patch = patchService.getPatchDTO(patchId, language);
        LOGGER.info("LOL patch retrieved");
        return new ResponseEntity<>(patch, HttpStatus.OK);
    }
}