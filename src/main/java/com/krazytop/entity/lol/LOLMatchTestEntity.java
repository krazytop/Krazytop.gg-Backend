package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import com.krazytop.repository.lol.LOLQueueNomenclatureRepository;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLMatchTestEntity {

    private String id;
    @JsonProperty("gameVersion")
    private String version;
    @JsonProperty("gameCreation")
    private Long datetime;
    @JsonProperty("gameDuration")
    private Long duration;
    private LOLQueueNomenclature queue;
    @JsonProperty("teams")
    private List<LOLTeamTestEntity> teams;
    private boolean remake;
    @JsonProperty("participants")
    private List<LOLParticipantTestEntity> participants;

    @JsonProperty("queueId")
    private void unpackQueue(String queueId) {
        LOLQueueNomenclatureRepository queueNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLQueueNomenclatureRepository.class);
        this.setQueue(queueNomenclatureRepository.findFirstById(queueId));
    }

    public void dispatchParticipantsInTeams() {
        this.getTeams().forEach(team -> team.setParticipants(this.participants.stream().filter(participant -> Objects.equals(participant.getTeamId(), team.getId())).toList()));
        this.setParticipants(null);
    }

}