package com.krazytop.service.tft;

import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import com.krazytop.repository.tft.TFTRankRepository;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class TFTRankService {

    private final TFTRankRepository rankRepository;
    private final RIOTMetadataService metadataService;
    private final RIOTRankService riotRankService;

    @Autowired
    public TFTRankService(TFTRankRepository rankRepository, RIOTMetadataService metadataService, RIOTRankService riotRankService) {
        this.rankRepository = rankRepository;
        this.metadataService = metadataService;
        this.riotRankService = riotRankService;
    }

    public RIOTRankEntity getLocalRank(String puuid) {
        return rankRepository.findFirstByPuuid(puuid);
    }

    public void updateRemoteToLocalRank(String puuid) throws URISyntaxException, IOException {
        int currentSet = metadataService.getMetadata().getCurrentTFTSet();
        riotRankService.updateRemoteToLocalRank(puuid, "https://euw1.api.riotgames.com/tft/league/v1/entries/by-summoner/%s?api_key=%s", currentSet, rankRepository);
    }

}
