package com.krazytop.entity.riot.rank;

import lombok.Data;

import java.util.*;

@Data
public class RIOTSeasonOrSetRanks {

    private Integer nb;
    private List<RIOTQueueRanks> queueRanks = new ArrayList<>();

    public RIOTSeasonOrSetRanks(int nb) {
        this.nb = nb;
    }

    public void joinRanks(List<RIOTRankInformations> newRanksInformations, String queueName) {
        RIOTQueueRanks queueRank = queueRanks.stream()
                .filter(q -> Objects.equals(q.getName(), queueName))
                .findFirst()
                .orElseGet(() -> {
                    RIOTQueueRanks newQueueRank = new RIOTQueueRanks(queueName);
                    queueRanks.add(newQueueRank);
                    return newQueueRank;
                });
        queueRank.joinRanks(newRanksInformations);
    }

}