package com.krazytop.entity.destiny;

import java.util.Arrays;
import java.util.Objects;

public enum DestinyPresentationTreeEnum {
    TITLES(616318467L),
    ARCHIVED_TITLES(1881970629L),
    CATALYST(2744330515L),
    KINETIC_WEAPON_MODELS(127506319L),
    ENERGY_WEAPON_MODELS(3289524180L),
    POWER_WEAPON_MODELS(1464475380L);

    final Long hash;

    DestinyPresentationTreeEnum(Long hash) {
        this.hash = hash;
    }

    public static boolean isUseful(Long hash) {
        return Arrays.stream(DestinyPresentationTreeEnum.values())
                .anyMatch(tree -> Objects.equals(tree.hash, hash));
    }
}
