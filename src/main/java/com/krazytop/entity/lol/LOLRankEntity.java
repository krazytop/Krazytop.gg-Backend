package com.krazytop.entity.lol;

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

    @JsonProperty("summonerId")
    private String summonerId;
    @JsonProperty("tier")
    private String tier;
    @JsonProperty("rank")
    private String rank;
    @JsonProperty("leaguePoints")
    private int leaguePoints;
    @JsonProperty("wins")
    private int wins;
    @JsonProperty("losses")
    private int losses;
    private Date updateDate;
    @JsonProperty("queueType")
    private String queue;

    public boolean equals(LOLRankEntity rank) {
        return getLeaguePoints() == rank.getLeaguePoints()
                && getWins() == rank.getWins() && getLosses() == rank.getLosses()
                && Objects.equals(getSummonerId(), rank.getSummonerId())
                && Objects.equals(getTier(), rank.getTier())
                && Objects.equals(getRank(), rank.getRank())
                && Objects.equals(getQueue(), rank.getQueue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSummonerId(), getTier(), getRank(), getQueue(), getLeaguePoints(), getUpdateDate(), getWins(), getLosses());
    }
}
