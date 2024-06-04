package com.krazytop.http_response.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLTeamHTTPResponse {

    @JsonProperty("bans")
    private LOLBansHTTPResponse bans;

    @JsonProperty("objectives")
    private LOLObjectivesHTTPResponse objectives;

    @JsonProperty("teamId")
    private String id;

    @JsonProperty("win")
    private boolean hasWin;
}
