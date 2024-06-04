package com.krazytop.entity.lol;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Data
@Document(collection = "Rank")
public class LOLRankEntity {

    private String summonerId;
    private String tier;
    private String rank;
    private String queueType;
    private int leaguePoints;
    private Date updateDate;
    private int wins;
    private int losses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LOLRankEntity that = (LOLRankEntity) o;
        return getLeaguePoints() == that.getLeaguePoints()
                && getWins() == that.getWins() && getLosses() == that.getLosses()
                && Objects.equals(getSummonerId(), that.getSummonerId())
                && Objects.equals(getTier(), that.getTier())
                && Objects.equals(getRank(), that.getRank())
                && Objects.equals(getQueueType(), that.getQueueType())
                && Objects.equals(getUpdateDate(), that.getUpdateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSummonerId(), getTier(), getRank(), getQueueType(), getLeaguePoints(), getUpdateDate(), getWins(), getLosses());
    }
}
