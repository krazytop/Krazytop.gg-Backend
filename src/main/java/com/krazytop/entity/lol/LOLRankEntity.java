package com.krazytop.entity.lol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.config.SpringConfiguration;
import com.krazytop.nomenclature.lol.LOLQueueNomenclature;
import com.krazytop.repository.lol.LOLQueueNomenclatureRepository;
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
    private LOLQueueNomenclature queue;

    @JsonProperty("queueType")
    private void unpackQueue(String queueType) {
        LOLQueueNomenclatureRepository queueNomenclatureRepository = SpringConfiguration.contextProvider().getApplicationContext().getBean(LOLQueueNomenclatureRepository.class);
//TODO set queue without type
        this.setQueue(queueNomenclatureRepository.findFirstByName(queueType));
    }

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
                && Objects.equals(getQueue(), that.getQueue())
                && Objects.equals(getUpdateDate(), that.getUpdateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSummonerId(), getTier(), getRank(), getQueue(), getLeaguePoints(), getUpdateDate(), getWins(), getLosses());
    }
}
