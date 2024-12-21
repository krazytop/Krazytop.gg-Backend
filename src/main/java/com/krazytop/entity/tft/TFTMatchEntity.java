package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTMatchEntity {

    @JsonAlias({"matchId", "gameId"})
    private String id;
    @JsonAlias({"game_version", "gameVersion"})
    private String version;
    @JsonAlias({"gameCreation", "gameCreatedAt", "game_datetime"})
    private Long datetime;//TODO to date
    @JsonAlias({"game_length", "gameLength"})
    private Long duration;
    @JsonAlias("tft_set_number")
    private Integer set;
    private List<TFTParticipantEntity> participants;
    @JsonAlias({"queueId", "queue_id"})
    private Integer queue;
    @JsonIgnore
    private List<String> owners = new ArrayList<>();

    @JsonProperty("season")
    private void unpackSet(String set) {
        this.set = Integer.valueOf(set.replace("set", "").replace(".5", ""));
    }

}