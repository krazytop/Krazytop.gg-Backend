package com.krazytop.api.lol;

import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.repository.lol.LOLRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class LOLRankApi {

    private final LOLRankRepository lolRankRepository;

    @Autowired
    public LOLRankApi(LOLRankRepository lolRankRepository) {
        this.lolRankRepository = lolRankRepository;
    }

    public LOLRankEntity getRank(String summonerId, String queueType) {
        return lolRankRepository.findFirstBySummonerIdAndQueueTypeOrderByUpdateDateDesc(summonerId, queueType);
    }


    public List<LOLRankEntity> updateRank(List<LOLRankEntity> ranks) {
        List<LOLRankEntity> ranksToUpdate = new ArrayList<>();

        for (LOLRankEntity rank : ranks) {
            rank.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            LOLRankEntity localRank = lolRankRepository.findFirstBySummonerIdOrderByUpdateDateDesc(rank.getSummonerId());

            if (localRank != null) {
                if (!rank.equals(localRank)) {
                    ranksToUpdate.add(rank);
                }
            } else {
                ranksToUpdate.add(rank);
            }
        }
        lolRankRepository.saveAll(ranksToUpdate);
        return ranks;
    }

}
