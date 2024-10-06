package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class CRUpcomingChestsEntity {

    @JsonAlias("items")
    private List<CRChestEntity> chests;

}
