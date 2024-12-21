package com.krazytop.entity.riot.rank;

import lombok.Data;

import java.util.*;

@Data
public class RIOTSeasonOrSetRanksEntity {

    private int nb;
    private List<RIOTQueueRanksEntity> queueRanks = new ArrayList<>();

    public RIOTSeasonOrSetRanksEntity(int nb) {
        this.nb = nb;
    }

    public void joinRanks(List<RIOTRankInformationsEntity> newRanksInformations, String queueName) {
        RIOTQueueRanksEntity queueRank = queueRanks.stream()
                .filter(q -> Objects.equals(q.getName(), queueName))
                .findFirst()
                .orElseGet(() -> {
                    RIOTQueueRanksEntity newQueueRank = new RIOTQueueRanksEntity(queueName);
                    queueRanks.add(newQueueRank);
                    return newQueueRank;
                });
        queueRank.joinRanks(newRanksInformations);
    }

}