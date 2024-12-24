package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"matchId", "shard"})
public class TFTMatchEntity {

    @JsonAlias("matchId")
    private String id;
    @JsonAlias({"game_version", "gameVersion"})
    private String version;
    private Date datetime;
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

    @JsonProperty("game_datetime")
    @JsonAlias("gameCreatedAt")
    private void unpackDateTime(Long datetime) {
        this.datetime = new Date(datetime);
    }

    @JsonProperty("shard")
    private void unpackShard(String shard) {
        this.id = shard.toUpperCase() + "_" + this.id;
    }

}