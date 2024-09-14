package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import com.krazytop.repository.lol.LOLQueueNomenclatureRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLMatchEntity {

    private String id;
    @JsonProperty("gameVersion")
    private String version;
    @JsonProperty("gameCreation")
    private Long datetime;
    @JsonProperty("gameDuration")
    private Long duration;
    private LOLQueueNomenclature queue;
    @JsonProperty("teams")
    private List<LOLTeamEntity> teams;
    private boolean remake;
    @JsonProperty("participants")
    @Transient
    private List<LOLParticipantEntity> participants;

    @JsonProperty("queueId")
    private void unpackQueue(String queueId) {
        LOLQueueNomenclatureRepository queueNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLQueueNomenclatureRepository.class);
        this.setQueue(queueNomenclatureRepository.findFirstById(queueId));
    }

    public void dispatchParticipantsInTeamsAndBuildSummoners() {
        this.getTeams().forEach(team -> team.setParticipants(this.participants.stream().filter(participant -> Objects.equals(participant.getTeamId(), team.getId())).toList()));
        this.getTeams().forEach(team -> team.getParticipants().forEach(LOLParticipantEntity::buildSummoner));
    }

}