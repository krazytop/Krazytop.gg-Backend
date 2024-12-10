package com.krazytop.service.lol;

import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class LOLRankService {

    private final LOLRankRepository rankRepository;
    private final RIOTMetadataService metadataService;
    private final RIOTRankService riotRankService;

    @Autowired
    public LOLRankService(LOLRankRepository rankRepository, RIOTMetadataService metadataService, RIOTRankService riotRankService) {
        this.rankRepository = rankRepository;
        this.metadataService = metadataService;
        this.riotRankService = riotRankService;
    }

    public RIOTRankEntity getLocalRank(String puuid) {
        return rankRepository.findFirstByPuuid(puuid);
    }

    public void updateRemoteToLocalRank(String puuid) throws URISyntaxException, IOException {
        int currentSeason = metadataService.getMetadata().getCurrentLOLSeason();
        riotRankService.updateRemoteToLocalRank(puuid, "https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s", currentSeason, rankRepository);
    }

}
