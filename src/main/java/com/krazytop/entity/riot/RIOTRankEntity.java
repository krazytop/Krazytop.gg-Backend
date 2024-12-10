package com.krazytop.entity.riot;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.krazytop.nomenclature.riot.RIOTRankEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Data
@Document(collection = "Rank")
@JsonDeserialize(using = RIOTRankEntity.CustomDeserializer.class)
@AllArgsConstructor
@NoArgsConstructor
public class RIOTRankEntity {

    @Id
    private String puuid;
    private Map<Integer, Map<String, List<RankInformations>>> ranks; // Map < setNb or seasonNb , Map < queueNb , List < RankInformations > > >

    @Data
    public static class RankInformations {

        private String tier;
        private String rank;
        private Integer leaguePoints;
        private Date date;
        private Integer wins;
        private Integer losses;

        public boolean needUpdating(RankInformations newRank) {
            return !Objects.equals(newRank.wins, this.wins) || !Objects.equals(newRank.losses, this.losses);
        }
    }

    static class CustomDeserializer extends JsonDeserializer<RIOTRankEntity> {

        @Override
        public RIOTRankEntity deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            JsonNode nodes = jsonParser.getCodec().readTree(jsonParser);

            RIOTRankEntity rankStorage = new RIOTRankEntity();
            rankStorage.ranks = new HashMap<>();
            Map<String, List<RankInformations>> queueRanks = new HashMap<>();
            nodes.forEach(node -> {
                List<String> compatiblesQueues = Arrays.stream(RIOTRankEnum.values()).map(RIOTRankEnum::getId).toList();
                if (compatiblesQueues.contains(node.get("queueType").asText())) {
                    RankInformations rankInformations = new RankInformations();
                    rankInformations.setWins(node.get("wins").asInt());
                    rankInformations.setLosses(node.get("losses").asInt());
                    if (node.get("leaguePoints") != null) {
                        rankInformations.setLeaguePoints(node.get("leaguePoints").asInt());
                    } else {
                        rankInformations.setLeaguePoints(node.get("ratedRating").asInt());
                    }
                    if (node.get("tier") != null) {
                        rankInformations.setTier(node.get("tier").asText());
                    } else {
                        rankInformations.setTier(node.get("ratedTier").asText());
                    }
                    if (node.get("rank") != null) {
                        rankInformations.setRank(node.get("rank").asText());
                    }
                    rankInformations.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                    queueRanks.put(node.get("queueType").asText(), List.of(rankInformations));
                }
            });
            rankStorage.ranks.put(-1, queueRanks);
            return rankStorage;
        }
    }

}
