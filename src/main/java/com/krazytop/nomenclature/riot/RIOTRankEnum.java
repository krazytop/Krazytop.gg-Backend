package com.krazytop.nomenclature.riot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RIOTRankEnum {
    RANKED_TFT("RANKED_TFT"),
    RANKED_TFT_DOUBLE_UP("RANKED_TFT_DOUBLE_UP"),
    RANKED_TFT_TURBO("RANKED_TFT_TURBO"),
    RANKED_SOLO("RANKED_SOLO_5x5"),
    RANKED_TEAM("RANKED_TEAM_5x5"),;

    private final String name;
}
