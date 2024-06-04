package com.krazytop.http_response.lol;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

import java.io.IOException;
import java.util.List;

@Data
@JsonDeserialize(using = LOLMatchHTTPResponse.MatchDeserializer.class)
public class LOLMatchHTTPResponse implements HTTPResponseInterface<LOLMatchEntity> {

    private String id;
    private Long datetimeHTTPResponse;
    private double length;
    private String queueIdHTTPResponse;
    private List<LOLTeamHTTPResponse> teamsHTTPResponse;
    private List<LOLParticipantHTTPResponse> participantsHTTPResponse;

    public static String getUrl(String matchId) {
        return "https://europe.api.riotgames.com/lol/match/v5/matches/" + matchId;
    }

    @Override
    public Class<LOLMatchEntity> getEntityClass() {
        return LOLMatchEntity.class;
    }

    public static class MatchDeserializer extends JsonDeserializer<LOLMatchHTTPResponse> {
        @Override
        public LOLMatchHTTPResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

            LOLMatchHTTPResponse match = new LOLMatchHTTPResponse();

            match.setId(rootNode.at("/metadata/matchId").asText());
            match.setDatetimeHTTPResponse(rootNode.at("/info/gameCreation").asLong());
            match.setLength(rootNode.at("/info/gameDuration").asDouble());
            match.setQueueIdHTTPResponse(rootNode.at("/info/queueId").asText());

            ObjectMapper objectMapper = new ObjectMapper();
            match.setParticipantsHTTPResponse(objectMapper.readValue(rootNode.at("/info/participants").toString(), new TypeReference<>() {}));
            match.setTeamsHTTPResponse(objectMapper.readValue(rootNode.at("/info/teams").toString(), new TypeReference<>() {}));

            return match;
        }
    }

}
