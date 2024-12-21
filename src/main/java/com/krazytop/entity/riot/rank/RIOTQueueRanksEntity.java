package com.krazytop.entity.riot.rank;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RIOTQueueRanksEntity {

    private String name;
    private List<RIOTRankInformationsEntity> rankInformations = new ArrayList<>();

    public RIOTQueueRanksEntity (String name) {
        this.name = name;
    }

    public void joinRanks(List<RIOTRankInformationsEntity> newRanks) {
        newRanks.forEach(newRank -> {
            if (rankInformations.stream().noneMatch(rank -> rank.getLosses() + rank.getWins() == newRank.getLosses() + newRank.getWins())) {
                rankInformations.add(newRank);
            }
        });
    }

}