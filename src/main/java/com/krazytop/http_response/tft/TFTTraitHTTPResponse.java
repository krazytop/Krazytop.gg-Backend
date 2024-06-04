package com.krazytop.http_response.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTTraitHTTPResponse {

    @JsonProperty("name")
    private String id;

    @JsonProperty("num_units")
    private int numberUnits;

    @JsonProperty("style")
    private int style;

    @JsonProperty("tier_current")
    private int currentTier;

    @JsonProperty("tier_total")
    private int maxTier;

}
