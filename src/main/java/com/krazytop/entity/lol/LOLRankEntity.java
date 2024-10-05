package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "Rank")
public class LOLRankEntity {

    private String summonerId;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private Date updateDate;
    @JsonAlias("queueType")
    private String queue;

    public boolean needToUpdate(LOLRankEntity rank) {
        return rank == null
                || getLeaguePoints() != rank.getLeaguePoints()
                || getWins() != rank.getWins()
                || getLosses() != rank.getLosses()
                || !Objects.equals(getTier(), rank.getTier())
                || !Objects.equals(getRank(), rank.getRank());
    }

}
