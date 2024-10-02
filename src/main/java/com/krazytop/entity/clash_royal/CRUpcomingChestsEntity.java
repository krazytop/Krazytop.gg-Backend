package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CRUpcomingChestsEntity {

    @JsonProperty("items")
    private List<CRChestEntity> chests;

}
