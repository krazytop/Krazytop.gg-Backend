package com.krazytop.service.lol;

import com.krazytop.api.lol.LOLMatchApi;
import com.krazytop.entity.lol.LOLMatchEntity;
import com.krazytop.entity.lol.LOLParticipantEntity;
import com.krazytop.entity.lol.LOLTeamEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LOLStatsService {

    private final LOLMatchApi lolMatchApi;

    @Autowired
    public LOLStatsService(LOLMatchApi lolMatchApi) {
        this.lolMatchApi = lolMatchApi;
    }

    public List<String> getLatestMatchesResult(String puuid, String queue, String role) {
        List<String> latestMatchesResult = new ArrayList<>();
        List<LOLMatchEntity> latestMatches = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // 5 pages
            latestMatches.addAll(lolMatchApi.getMatches(puuid, i, queue, role));
        }
        for (LOLMatchEntity match : latestMatches) {
            if (match.isRemake()) {
                latestMatchesResult.add("REMAKE");
            } else {
                for (LOLTeamEntity team : match.getTeams()) {
                    for (LOLParticipantEntity participant : team.getParticipants()) {
                        if (Objects.equals(participant.getSummoner().getPuuid(), puuid)) {
                            latestMatchesResult.add(team.isHasWin() ? "VICTORY" : "DEFEAT");
                            break;
                        }
                    }
                }
            }
        }
        return latestMatchesResult;
    }


}
