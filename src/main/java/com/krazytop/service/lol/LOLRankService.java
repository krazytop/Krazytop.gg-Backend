package com.krazytop.service.lol;

import com.krazytop.api.lol.LOLRankApi;
import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.http_response.lol.LOLRankHTTPResponse;
import com.krazytop.service.riot.RIOTApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LOLRankService {

    private final LOLRankApi lolRankApi;
    private final RIOTApiService riotApiService;

    @Autowired
    public LOLRankService(LOLRankApi lolRankApi, RIOTApiService riotApiService) {
        this.lolRankApi = lolRankApi;
        this.riotApiService = riotApiService;
    }

    public LOLRankEntity getLocalRank(String summonerId, String queueType) {
        return lolRankApi.getRank(summonerId, queueType);
    }

    public List<LOLRankEntity> updateRemoteToLocalRank(String summonerId) {
        String apiUrl = LOLRankHTTPResponse.getUrl(summonerId);
        List<LOLRankEntity> ranks = riotApiService.callRiotApiForList(apiUrl, LOLRankHTTPResponse.class);
        for (String queueType : List.of("RANKED_SOLO_5x5", "RANKED_FLEX_SR")) {
            boolean found = false;
            for (LOLRankEntity rank : ranks) {
                if (rank.getQueueType().equals(queueType)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                LOLRankEntity newRank = new LOLRankEntity();
                newRank.setQueueType(queueType);
                ranks.add(newRank);
            }
        }
        return lolRankApi.updateRank(ranks);
    }

}
