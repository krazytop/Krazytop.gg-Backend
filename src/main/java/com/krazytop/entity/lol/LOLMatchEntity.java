package com.krazytop.entity.lol;

import com.krazytop.http_response.lol.LOLTeamHTTPResponse;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Match")
public class LOLMatchEntity {

    private String id;
    private Long datetime;
    private double length;
    private String queueIdHTTPResponse;
    private LOLQueueNomenclature queue;
    private List<LOLTeamHTTPResponse> teamsHTTPResponse;
    private List<LOLTeamEntity> teams;
    private boolean remake;
    private List<LOLParticipantEntity> participantsHTTPResponse;

    public String toString() {
        return this.id;
    }

}
