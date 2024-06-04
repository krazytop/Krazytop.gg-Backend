package com.krazytop.http_response.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = LOLBansHTTPResponse.BansDeserializer.class)
public class LOLBansHTTPResponse {

    private List<String> bansChampionId;

    public static class BansDeserializer extends JsonDeserializer<LOLBansHTTPResponse> {
        @Override
        public LOLBansHTTPResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            JsonNode bansNode = jsonParser.getCodec().readTree(jsonParser);

            LOLBansHTTPResponse bansHTTPResponse = new LOLBansHTTPResponse();
            List<String> bansChampionId = new ArrayList<>();

            for (JsonNode banNode : bansNode) {
                String championId = banNode.get("championId").asText();
                bansChampionId.add(championId);
            }

            bansHTTPResponse.setBansChampionId(bansChampionId);
            return bansHTTPResponse;
        }
    }
}
