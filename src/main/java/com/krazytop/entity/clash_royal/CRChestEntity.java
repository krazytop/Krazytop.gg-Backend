package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CRChestEntity {

    @JsonProperty("index")
    private int index;

    @JsonProperty("name")
    private String name;
}
