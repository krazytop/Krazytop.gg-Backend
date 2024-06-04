package com.krazytop.entity.clash_royal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CRBadgeEntity {

    @JsonProperty("name")
    private String name;

    @JsonProperty("level")
    private int level;

    @JsonProperty("maxLevel")
    private int maxLevel;

    @JsonProperty("progress")
    private int progress;

    @JsonProperty("target")
    private int target;

    @Transient
    @JsonProperty("iconUrls")
    private CRBadgeImageEntity iconUrls;

    private String image;

}
