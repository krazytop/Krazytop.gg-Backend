package com.krazytop.service.tft;

import com.krazytop.api.tft.TFTMatchApi;
import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.entity.tft.TFTParticipantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TFTStatsService {

    private final TFTMatchApi tftMatchApi;

    @Autowired
    public TFTStatsService(TFTMatchApi tftMatchApi) {
        this.tftMatchApi = tftMatchApi;
    }

    public List<Integer> getLatestMatchesPlacement(String puuid, String queue, String set) {
        List<Integer> latestMatchesPlacement = new ArrayList<>();
        List<TFTMatchEntity> latestMatches = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // 5 pages
            latestMatches.addAll(tftMatchApi.getMatches(puuid, i, queue, set));
        }
        for (TFTMatchEntity match : latestMatches) {
            for (TFTParticipantEntity participant : match.getParticipants()) {
                if (Objects.equals(participant.getPuuid(), puuid)) {
                    latestMatchesPlacement.add(participant.getPlacement());
                }
            }
        }
        return latestMatchesPlacement;
    }


}
