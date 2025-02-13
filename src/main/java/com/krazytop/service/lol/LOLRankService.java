package com.krazytop.service.lol;

import com.krazytop.nomenclature.GameEnum;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class LOLRankService {

    private final RIOTMetadataService metadataService;
    private final RIOTRankService riotRankService;

    @Autowired
    public LOLRankService(RIOTMetadataService metadataService, RIOTRankService riotRankService) {
        this.metadataService = metadataService;
        this.riotRankService = riotRankService;
    }

    public void updateRanks(String puuid) throws URISyntaxException, IOException {
        /**if (riotRankService.getRanks(puuid, GameEnum.TFT).isEmpty()) {
            updateLegacyRanksFromLOLChess(puuid);
        }**/
        riotRankService.updateRecentRanks(puuid, GameEnum.LOL);
    }

}
