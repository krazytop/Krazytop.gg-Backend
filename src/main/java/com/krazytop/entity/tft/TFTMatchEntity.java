package com.krazytop.entity.tft;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.nomenclature.tft.TFTQueueNomenclature;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TFTMatchEntity {

    private String id;
    @JsonAlias("game_version")
    private String version;
    @JsonAlias({"gameCreation", "game_datetime"})
    private Long datetime;
    @JsonAlias("game_length")
    private Long duration;
    @JsonAlias("tft_set_number")
    private Long set;
    private List<TFTParticipantEntity> participants;
    private TFTQueueNomenclature queue;
    private List<String> owners = new ArrayList<>();//TODO JSON IGNORE pour ne pas envoyer au front (lol aussi)

    @JsonProperty("queue_id")
    private void unpackQueue(String queueId) {

    }

}