package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String version;
    private Long datetime;
    private Long duration;
    private LOLQueueNomenclature queue;

    private List<LOLTeamTestEntity> teams;
    private boolean remake;
}