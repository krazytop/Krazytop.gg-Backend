package com.krazytop.http_response.tft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTUnitHTTPResponse {

    @JsonProperty("character_id")
    private String id;

    @JsonProperty("itemNames")
    private List<String> itemsIds;

    @JsonProperty("rarity")
    private int rarity;

    @JsonProperty("tier")
    private int tier;

}
