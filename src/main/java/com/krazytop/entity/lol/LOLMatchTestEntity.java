package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.http_response.lol.LOLTeamHTTPResponse;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLMatchTestEntity {

    private String id;
    @JsonProperty("gameVersion")
    private String version;
    @JsonProperty("gameCreation")
    private Long datetime;
    @JsonProperty("gameDuration")
    private Long duration;
    private LOLQueueNomenclature queue;
    private List<LOLTeamTestEntity> teams;
    private boolean remake;
}