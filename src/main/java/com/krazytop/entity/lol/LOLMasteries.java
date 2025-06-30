package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Mastery")
public class LOLMasteries {

    @Id private String puuid;
    private List<LOLMastery> champions = new ArrayList<>();

    public LOLMasteries(String puuid){
        this.puuid = puuid;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LOLMastery {

        @JsonAlias("championLevel")
        private int level;
        @JsonAlias("championPoints")
        private int points;
        @JsonAlias("championId")
        private String champion;
        private Date lastPlayTime;

        @JsonProperty("lastPlayTime")
        private void unpackLastPlayTime(JsonNode node) {
            this.lastPlayTime = new Date(node.asLong());
        }
    }
}

