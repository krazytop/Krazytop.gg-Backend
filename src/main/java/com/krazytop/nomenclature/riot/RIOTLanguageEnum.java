package com.krazytop.nomenclature.riot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RIOTLanguageEnum {
    FR_FR("fr_FR", "Français"),
    EN_GB("en_GB", "English");

    private final String path;
    private final String frontRiotPath;
}
