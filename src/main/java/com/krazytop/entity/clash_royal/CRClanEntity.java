package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRClanEntity {

    @JsonAlias("tag")
    private String id;
    private String name;
    @JsonAlias("badgeId")
    private int badge;
}
