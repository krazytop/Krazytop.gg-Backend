package com.krazytop.controller;

import com.krazytop.api_gateway.api.generated.LanguageApi;
import com.krazytop.api_gateway.model.generated.LanguageDTO;
import com.krazytop.nomenclature.LanguageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.krazytop.nomenclature.LanguageService.SUPPORTED_LANGUAGES;

@RestController
public class LanguageController implements LanguageApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguageController.class);

    @Override
    public ResponseEntity<List<LanguageDTO>> getAllSupportedLanguages() {
        LOGGER.info("Retrieving all supported languages");
        List<LanguageDTO> supportedLanguages = SUPPORTED_LANGUAGES;
        LOGGER.info("All supported languages retrieved");
        return new ResponseEntity<>(supportedLanguages, HttpStatus.OK);
    }
}