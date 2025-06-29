package com.krazytop.entity.riot.rank;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Data
@Document(collection = "Rank")
public class RIOTRank {

    @Id
    private String puuid;
    private List<RIOTSeasonOrSetRanksEntity> seasonOrSetRanks = new ArrayList<>();

    public RIOTRank(String puuid) {
        this.puuid = puuid;
    }

    public void joinRanks(List<RIOTRankInformationsEntity> newRanksInformations, int seasonOrSetNb, String queueName) {
        RIOTSeasonOrSetRanksEntity seasonOrSetRank = seasonOrSetRanks.stream()
                .filter(s -> Objects.equals(s.getNb(), seasonOrSetNb))
                .findFirst()
                .orElseGet(() -> {
                    RIOTSeasonOrSetRanksEntity newSeasonOrSetRank = new RIOTSeasonOrSetRanksEntity(seasonOrSetNb);
                    seasonOrSetRanks.add(newSeasonOrSetRank);
                    return newSeasonOrSetRank;
                });
        seasonOrSetRank.joinRanks(newRanksInformations, queueName);
    }

}