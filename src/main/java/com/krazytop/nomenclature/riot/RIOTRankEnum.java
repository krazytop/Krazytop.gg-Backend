package com.krazytop.nomenclature.riot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RIOTRankEnum {
    RANKED_TFT("RANKED_TFT", "Solo Ranked", false),
    RANKED_TFT_DOUBLE_UP("RANKED_TFT_DOUBLE_UP", "Double Up", false),
    RANKED_TFT_TURBO("RANKED_TFT_TURBO", "Hyper Roll", false),
    RANKED_SOLO("RANKED_SOLO_5x5", "Solo Ranked", true),
    RANKED_TEAM("RANKED_TEAM_5x5", "Flex Ranked", true);

    private final String id;
    private final String name;
    private final Boolean isLOL;
}
