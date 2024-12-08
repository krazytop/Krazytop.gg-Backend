package com.krazytop.service.tft;

import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.repository.tft.TFTRankRepository;
import com.krazytop.repository.tft.TFTVersionRepository;
import com.krazytop.service.riot.RIOTRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class TFTRankService {

    private final TFTRankRepository rankRepository;
    private final TFTVersionRepository versionRepository;
    private final RIOTRankService riotRankService;

    @Autowired
    public TFTRankService(TFTRankRepository rankRepository, TFTVersionRepository versionRepository, RIOTRankService riotRankService) {
        this.rankRepository = rankRepository;
        this.versionRepository = versionRepository;
        this.riotRankService = riotRankService;
    }

    public RIOTRankEntity getLocalRank(String puuid) {
        return rankRepository.findFirstByPuuid(puuid);
    }

    public void updateRemoteToLocalRank(String puuid) throws URISyntaxException, IOException {
        int currentSet = versionRepository.findFirstByOrderByOfficialVersionAsc().getCurrentSet();
        riotRankService.updateRemoteToLocalRank(puuid, "https://euw1.api.riotgames.com/tft/league/v1/entries/by-summoner/%s?api_key=%s", currentSet, rankRepository);
    }

}
