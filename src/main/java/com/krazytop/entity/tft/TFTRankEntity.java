package com.krazytop.entity.tft;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Data
@Document(collection = "Rank")
public class TFTRankEntity {

    private String summonerId;
    private String tier;
    private String rank;
    private String queueType;
    private int leaguePoints;
    private Date updateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TFTRankEntity rank1 = (TFTRankEntity) o;
        return getLeaguePoints() == rank1.getLeaguePoints()
                && Objects.equals(getSummonerId(), rank1.getSummonerId())
                && Objects.equals(getTier(), rank1.getTier())
                && Objects.equals(getRank(), rank1.getRank())
                && Objects.equals(getQueueType(), rank1.getQueueType());
    }

    @Override
    public int hashCode() {//TODO utile ?
        return Objects.hash(getSummonerId(), getTier(), getRank(), getLeaguePoints(), getQueueType());
    }

}
