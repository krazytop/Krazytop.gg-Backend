package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import com.krazytop.repository.lol.LOLQueueNomenclatureRepository;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLMatchEntity {

    private String id;
    @JsonAlias("gameVersion")
    private String version;
    @JsonAlias("gameCreation")
    private Long datetime;
    @JsonAlias("gameDuration")
    private Long duration;
    private List<LOLTeamEntity> teams;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<LOLParticipantEntity> participants;
    private LOLQueueNomenclature queue;
    private boolean remake;
    private List<String> owners = new ArrayList<>();

    @JsonProperty("queueId")
    private void unpackQueue(String queueId) {
        LOLQueueNomenclatureRepository queueNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLQueueNomenclatureRepository.class);
        this.setQueue(queueNomenclatureRepository.findFirstById(queueId));
    }

    public void dispatchParticipantsInTeamsAndBuildSummoners() {
        this.getTeams().forEach(team -> team.setParticipants(this.participants.stream().filter(participant -> Objects.equals(participant.getTeamId(), team.getId())).toList()));
        this.getTeams().forEach(team -> team.getParticipants().forEach(LOLParticipantEntity::buildSummoner));
    }

    public void buildArenaMatch() {
        this.teams = new ArrayList<>();
        this.participants.forEach(participant -> {
            Optional<LOLTeamEntity> optParticipantTeam = this.teams.stream()
                    .filter(team -> Objects.equals(team.getId(), participant.getSubTeamId()))
                    .findFirst();
            LOLTeamEntity participantTeam;
            if (optParticipantTeam.isPresent()) {
                participantTeam = optParticipantTeam.get();
                participantTeam.getParticipants().add(participant);
            } else {
                participantTeam = new LOLTeamEntity();
                participantTeam.setId(participant.getSubTeamId());
                participantTeam.setParticipants(new ArrayList<>(List.of(participant)));
                participantTeam.setPlacement(participant.getPlacement());
                participantTeam.setHasWin(participant.getPlacement() <= participants.size()/2);
                this.teams.add(participantTeam);
            }
        });
    }

}