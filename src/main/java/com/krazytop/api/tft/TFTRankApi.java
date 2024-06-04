package com.krazytop.api.tft;

import com.krazytop.entity.tft.TFTRankEntity;
import com.krazytop.repository.tft.TFTRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class TFTRankApi {

    private final TFTRankRepository tftRankRepository;

    @Autowired
    public TFTRankApi(TFTRankRepository tftRankRepository) {
        this.tftRankRepository = tftRankRepository;
    }

    public TFTRankEntity getRank(String summonerId, String queueType) {
        return tftRankRepository.findFirstBySummonerIdAndQueueTypeOrderByUpdateDateDesc(summonerId, queueType);
    }


    public List<TFTRankEntity> updateRank(List<TFTRankEntity> ranks) {
        List<TFTRankEntity> ranksToUpdate = new ArrayList<>();

        for (TFTRankEntity rank : ranks) {
            rank.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            TFTRankEntity localRank = tftRankRepository.findFirstBySummonerIdOrderByUpdateDateDesc(rank.getSummonerId());

            if (localRank != null) {
                if (!rank.equals(localRank)) {
                    ranksToUpdate.add(rank);
                }
            } else {
                ranksToUpdate.add(rank);
            }
        }
        tftRankRepository.saveAll(ranksToUpdate);
        return ranks;
    }

}
