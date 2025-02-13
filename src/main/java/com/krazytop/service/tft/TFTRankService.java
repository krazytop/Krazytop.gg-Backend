package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.entity.riot.rank.RIOTRankInformationsEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.nomenclature.tft.TFTQueueEnum;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTRankService;
import com.krazytop.service.riot.RIOTSummonerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class TFTRankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTRankService.class);

    private final RIOTMetadataService metadataService;
    private final RIOTRankService riotRankService;
    private final RIOTSummonerService summonerService;

    @Autowired
    public TFTRankService(RIOTMetadataService metadataService, RIOTRankService riotRankService, RIOTSummonerService summonerService) {
        this.metadataService = metadataService;
        this.riotRankService = riotRankService;
        this.summonerService = summonerService;
    }

    public void updateRanks(String puuid) throws URISyntaxException, IOException {//TODO changer le puuid par celui de mon api et le scraping devrait fonctionner
        /**if (riotRankService.getRanks(puuid, GameEnum.TFT).isEmpty()) {
            updateLegacyRanksFromLOLChess(puuid);
        }**/
        riotRankService.updateRecentRanks(puuid, GameEnum.TFT);
    }

    public void updateLegacyRanksFromLOLChess(String puuid) throws URISyntaxException, IOException {
        LOGGER.info("Updating legacy TFT ranks from LOLChess");
        RIOTMetadataEntity metadata = metadataService.getMetadata();
        RIOTSummonerEntity summoner = summonerService.getRemoteSummonerByPuuid(puuid);
        int latestSet = metadata.getCurrentTFTSet();
        int setNb = 1;
        while (setNb <= latestSet) {
            for (TFTQueueEnum queue : TFTQueueEnum.getAllRankedQueues()) {
                for (String queueId : queue.getIds()) {
                    int pageNb = 1;
                    boolean lastPage = false;
                    while (!lastPage) {
                        String url = String.format("https://tft.dakgg.io/api/v1/summoners/euw1/%s-%s/league-logs?season=set%d&page=%d&size=100&queueId=%s", summoner.getName(), summoner.getTag(), setNb, pageNb, queueId);
                        ObjectMapper mapper = new ObjectMapper();
                        List<List<JsonNode>> nodes = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("summonerLeagueLogs"), new TypeReference<>() {});
                        List<RIOTRankInformationsEntity> newRanks = nodes.stream().map(node ->
                                new RIOTRankInformationsEntity(node.get(0).asLong(), node.get(1).asText(), node.get(2).asText(), node.get(3).asInt(), node.get(4).asInt(), node.get(5).asInt())).toList();
                        riotRankService.joinRanks(puuid, newRanks, setNb, queue.getRank().getId(), GameEnum.TFT);
                        lastPage = newRanks.isEmpty();
                        pageNb++;
                    }
                }
            }
            setNb++;
        }
    }





}
