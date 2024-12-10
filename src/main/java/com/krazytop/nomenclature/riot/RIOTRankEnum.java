package com.krazytop.nomenclature.riot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RIOTRankEnum {
    RANKED_TFT("RANKED_TFT", "Solo Ranked"),
    RANKED_TFT_DOUBLE_UP("RANKED_TFT_DOUBLE_UP", "Double Up"),
    RANKED_TFT_TURBO("RANKED_TFT_TURBO", "Hyper Roll"),
    RANKED_SOLO("RANKED_SOLO_5x5", "Solo Ranked"),
    RANKED_TEAM("RANKED_TEAM_5x5", "Flex Ranked");

    private final String id;
    private final String name;
}
