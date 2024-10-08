package com.krazytop.nomenclature.lol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LOLRoleEnum {
    TOP("top", "TOP"),
    JUNGLE("jungle", "JUNGLE"),
    MIDDLE("middle", "MIDDLE"),
    BOTTOM("bottom", "BOTTOM"),
    SUPPORT("support", "UTILITIES"),
    ALL_ROLES("all-roles", "ALL_ROLES");

    private final String name;
    private final String riotName;

    public static LOLRoleEnum fromName(String name) {
        for (LOLRoleEnum role : LOLRoleEnum.values()) {
            if (role.getName().equals(name)) {
                return role;
            }
        }
        return ALL_ROLES;
    }

}
