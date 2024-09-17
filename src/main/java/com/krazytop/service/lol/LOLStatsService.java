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
import java.util.stream.IntStream;

@Service
public class LOLStatsService {

    private final LOLMatchApi lolMatchApi;

    @Autowired
    public LOLStatsService(LOLMatchApi lolMatchApi) {
        this.lolMatchApi = lolMatchApi;
    }

    public List<String> getLatestMatchesResult(String puuid, String queue, String role) {
        List<String> latestMatchesResult = new ArrayList<>();
        List<LOLMatchEntity> latestMatches = IntStream.range(0, 5)
                .mapToObj(i -> lolMatchApi.getMatches(puuid, i, queue, role))
                .flatMap(List::stream)
                .toList();
        for (LOLMatchEntity match : latestMatches) {
            if (match.isRemake()) {
                latestMatchesResult.add("REMAKE");
            } else {
                match.getTeams().forEach(team -> team.getParticipants().forEach(participant -> {
                    if (Objects.equals(participant.getSummoner().getPuuid(), puuid)) {
                        latestMatchesResult.add(team.isHasWin() ? "VICTORY" : "DEFEAT");
                    }
                }));
            }
        }
        return latestMatchesResult;
    }


}
