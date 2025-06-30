package com.krazytop.entity.riot.rank;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RIOTQueueRanks {

    private String name;
    private List<RIOTRankInformations> rankInformations = new ArrayList<>();

    public RIOTQueueRanks(String name) {
        this.name = name;
    }

    public void joinRanks(List<RIOTRankInformations> newRanks) {
        newRanks.forEach(newRank -> {
            if (rankInformations.stream().noneMatch(rank -> rank.getLosses() + rank.getWins() == newRank.getLosses() + newRank.getWins())) {
                rankInformations.add(newRank);
            }
        });
    }

}