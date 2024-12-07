package com.krazytop.service.lol;

import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.lol.LOLVersionRepository;
import com.krazytop.service.riot.RIOTRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class LOLRankService {

    private final LOLRankRepository rankRepository;
    private final LOLVersionRepository versionRepository;
    private final RIOTRankService riotRankService;

    @Autowired
    public LOLRankService(LOLRankRepository rankRepository, LOLVersionRepository versionRepository, RIOTRankService riotRankService) {
        this.rankRepository = rankRepository;
        this.versionRepository = versionRepository;
        this.riotRankService = riotRankService;
    }

    public RIOTRankEntity getLocalRank(String puuid) {
        return rankRepository.findFirstByPuuid(puuid);
    }

    public void updateRemoteToLocalRank(String puuid) throws URISyntaxException, IOException {
        int currentSeason = versionRepository.findFirstByOrderByItemAsc().getCurrentSeason();
        riotRankService.updateRemoteToLocalRank(puuid, "https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s", currentSeason, rankRepository);
    }

}
