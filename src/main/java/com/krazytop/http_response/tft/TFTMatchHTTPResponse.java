package com.krazytop.http_response.tft;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

import java.io.IOException;
import java.util.List;

@Data
@JsonDeserialize(using = TFTMatchHTTPResponse.MatchDeserializer.class)
public class TFTMatchHTTPResponse implements HTTPResponseInterface<TFTMatchEntity> {

    private String id;
    private Long datetime;
    private double length;
    private String gameType;
    private String queueId;
    private String set;
    private List<TFTParticipantHTTPResponse> participantsHTTPResponse;

    public static String getUrl(String matchId) {
        return "https://europe.api.riotgames.com/tft/match/v1/matches/" + matchId;
    }

    @Override
    public Class<TFTMatchEntity> getEntityClass() {
        return TFTMatchEntity.class;
    }

    public static class MatchDeserializer extends JsonDeserializer<TFTMatchHTTPResponse> {
        @Override
        public TFTMatchHTTPResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

            TFTMatchHTTPResponse match = new TFTMatchHTTPResponse();

            match.setId(rootNode.at("/metadata/match_id").asText());
            match.setDatetime(Long.valueOf(rootNode.at("/info/game_datetime").asText()));
            match.setLength(Double.parseDouble(rootNode.at("/info/game_length").asText()));
            match.setGameType(rootNode.at("/info/tft_game_type").asText());
            match.setQueueId(rootNode.at("/info/queue_id").asText());
            match.setSet(rootNode.at("/info/tft_set_core_name").asText());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode participantsNode = rootNode.at("/info/participants");
            List<TFTParticipantHTTPResponse> participants = objectMapper.readValue(participantsNode.toString(), new TypeReference<>() {
            });

            match.setParticipantsHTTPResponse(participants);
            return match;
        }
    }

}
