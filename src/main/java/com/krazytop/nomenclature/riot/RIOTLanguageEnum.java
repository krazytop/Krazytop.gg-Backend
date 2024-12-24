package com.krazytop.nomenclature.riot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RIOTLanguageEnum {
    FR_FR("fr_FR"),
    EN_GB("en_GB");

    private final String path;
}
