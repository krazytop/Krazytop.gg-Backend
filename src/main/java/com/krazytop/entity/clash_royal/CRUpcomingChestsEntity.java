package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CRUpcomingChestsEntity {

    @JsonProperty("items")
    private List<CRChestEntity> chests;

    public static String getUrl(String playerId) {
        return "https://api.clashroyale.com/v1/players/" + playerId + "/upcomingchests";
    }
}
