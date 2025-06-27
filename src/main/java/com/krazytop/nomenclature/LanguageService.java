package com.krazytop.nomenclature;

import com.krazytop.api_gateway.model.generated.LanguageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
//TODO destiny path patch
    public static List<LanguageDTO> SUPPORTED_LANGUAGES = List.of(
            new LanguageDTO("Français", "fr_FR"),
            new LanguageDTO("English", "en_GB")
    );
}
