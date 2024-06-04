package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRClanEntity {

    @JsonProperty("tag")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("badgeId")
    private int badge;
}
