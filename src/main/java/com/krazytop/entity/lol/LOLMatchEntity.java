package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "Match")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LOLMatchEntity {

    private String id;
    private String version;
    private Date datetime;
    @JsonAlias("gameDuration")
    private Long duration;
    private List<LOLTeamEntity> teams;
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<LOLParticipantEntity> participants;
    private Boolean remake;
    @JsonAlias("queueId")
    private String queue;
    @JsonIgnore
    private List<String> owners = new ArrayList<>();

    public void dispatchParticipantsInTeamsNormalGame() {
        this.getTeams().forEach(team -> team.setParticipants(this.participants.stream().filter(participant -> Objects.equals(participant.getTeamId(), team.getId())).toList()));
    }

    public void dispatchParticipantsInTeamsArena() {
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
                participantTeam.setHasWin(participant.getPlacement() <= participants.size()/4);
                this.teams.add(participantTeam);
            }
        });
    }

    @JsonProperty("gameStartTimestamp")
    private void unpackDateTime(Long datetime) {
        this.datetime = new Date(datetime);
    }

    @JsonProperty("gameVersion")
    private void unpackVersion(String version) {
        this.version = version.replaceAll("^([^.]+\\.[^.]+)\\..*$", "$1");
    }
}