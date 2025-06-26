package com.krazytop.controller.lol;

import com.krazytop.api_gateway.api.generated.LolApi;
import com.krazytop.api_gateway.model.generated.LOLPatchDTO;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.mapper.lol.LOLPatchMapper;
import com.krazytop.nomenclature.lol.LOLPatch;
import com.krazytop.service.lol.LOLPatchService;
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
public class LOLPatchController implements LolApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLPatchController.class);

    private final LOLPatchService patchService;
    private final LOLPatchMapper patchMapper;

    @Autowired
    public LOLPatchController(LOLPatchService patchService, LOLPatchMapper patchMapper){
        this.patchService = patchService;
        this.patchMapper = patchMapper;
    }

    public ResponseEntity<String> updateAllPatches() {
        try {
            LOGGER.info("Updating all LOL patches");
            patchService.updateAllPatches();
            return new ResponseEntity<>("All LOL patches are up to date", HttpStatus.OK);
        } catch (IOException | URISyntaxException ex) {
            throw new CustomException(ApiErrorEnum.PATCH_UPDATE_ERROR, ex);
        }
    }

    public ResponseEntity<LOLPatchDTO> getPatch(String patchId, String language) {
        LOGGER.info("Retrieving LOL patch");
        Optional<LOLPatch> patch = patchService.getPatch(patchId, language);
        if (patch.isPresent()) {
            LOGGER.info("LOL patch retrieved");
            return new ResponseEntity<>(patchMapper.toDTO(patch.get()), HttpStatus.OK);
        } else {
            LOGGER.info("LOL patch not found");
            throw new CustomException(ApiErrorEnum.PATCH_NOT_FOUND);
        }
    }
}