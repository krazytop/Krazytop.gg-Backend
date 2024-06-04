package com.krazytop.http_response.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTParticipantHTTPResponse {

    @JsonProperty("puuid")
    private String puuid;

    @JsonProperty("augments")
    private List<String> augmentsIds;

    @JsonProperty("last_round")
    private int lastRound;

    @JsonProperty("level")
    private int level;

    @JsonProperty("placement")
    private int placement;

    @JsonProperty("time_eliminated")
    private float lifetime;

    @JsonProperty("units")
    private List<TFTUnitHTTPResponse> units;

    @JsonProperty("traits")
    private List<TFTTraitHTTPResponse> traits;
}
