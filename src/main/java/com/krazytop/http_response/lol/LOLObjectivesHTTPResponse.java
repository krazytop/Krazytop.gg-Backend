package com.krazytop.http_response.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.IOException;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = LOLObjectivesHTTPResponse.ObjectivesDeserializer.class)
public class LOLObjectivesHTTPResponse {

    private int baronKills;
    private int championKills;
    private int dragonKills;
    private int hordeKills;
    private int inhibitorKills;
    private int riftHeraldKills;
    private int towerKills;

    public static class ObjectivesDeserializer extends JsonDeserializer<LOLObjectivesHTTPResponse> {
        @Override
        public LOLObjectivesHTTPResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode objectivesNode = jsonParser.getCodec().readTree(jsonParser);

            LOLObjectivesHTTPResponse objectivesHTTPResponse = new LOLObjectivesHTTPResponse();
            objectivesHTTPResponse.setBaronKills(objectivesNode.at("/baron/kills").asInt());
            objectivesHTTPResponse.setChampionKills(objectivesNode.at("/champion/kills").asInt());
            objectivesHTTPResponse.setDragonKills(objectivesNode.at("/dragon/kills").asInt());
            objectivesHTTPResponse.setHordeKills(objectivesNode.at("/horde/kills").asInt());
            objectivesHTTPResponse.setInhibitorKills(objectivesNode.at("/inhibitor/kills").asInt());
            objectivesHTTPResponse.setRiftHeraldKills(objectivesNode.at("/riftHerald/kills").asInt());
            objectivesHTTPResponse.setTowerKills(objectivesNode.at("/tower/kills").asInt());

            return objectivesHTTPResponse;
        }
    }
}
