package com.krazytop.service.tft;

import com.krazytop.api.tft.TFTRankApi;
import com.krazytop.entity.tft.TFTRankEntity;
import com.krazytop.http_response.tft.TFTRankHTTPResponse;
import com.krazytop.service.riot.RIOTApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TFTRankService {

    private final TFTRankApi tftRankApi;
    private final RIOTApiService riotApiService;

    @Autowired
    public TFTRankService(TFTRankApi tftRankApi, RIOTApiService riotApiService) {
        this.tftRankApi = tftRankApi;
        this.riotApiService = riotApiService;
    }

    public TFTRankEntity getLocalRank(String summonerId, String queueType) {
        return tftRankApi.getRank(summonerId, queueType);
    }

    public List<TFTRankEntity> updateRemoteToLocalRank(String summonerId) {
        String apiUrl = TFTRankHTTPResponse.getUrl(summonerId);
        List<TFTRankEntity> ranks = riotApiService.callRiotApiForList(apiUrl, TFTRankHTTPResponse.class);
        for (String queueType : List.of("RANKED_TFT", "RANKED_TFT-TURBO", "RANKED_TFT_DOUBLE_UP")) {
            boolean found = false;
            for (TFTRankEntity rank : ranks) {
                if (rank.getQueueType().equals(queueType)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                TFTRankEntity newRank = new TFTRankEntity();
                newRank.setQueueType(queueType);
                ranks.add(newRank);
            }
        }
        return tftRankApi.updateRank(ranks);
    }

}
