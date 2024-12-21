package com.krazytop.service.riot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.entity.riot.rank.RIOTRankInformationsEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import com.krazytop.repository.tft.TFTRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class RIOTRankService {

    private final ApiKeyRepository apiKeyRepository;
    private final RIOTSummonerRepository summonerRepository;
    private final TFTRankRepository tftRankRepository;
    private final LOLRankRepository lolRankRepository;
    private final RIOTMetadataService metadataService;

    @Autowired
    public RIOTRankService(ApiKeyRepository apiKeyRepository, RIOTSummonerRepository summonerRepository, TFTRankRepository tftRankRepository, LOLRankRepository lolRankRepository, RIOTMetadataService metadataService) {
        this.apiKeyRepository = apiKeyRepository;
        this.summonerRepository = summonerRepository;
        this.tftRankRepository = tftRankRepository;
        this.lolRankRepository = lolRankRepository;
        this.metadataService = metadataService;
    }

    public Optional<RIOTRankEntity> getRanks(String puuid, GameEnum game) {
        return getRepository(game).findFirstByPuuid(puuid);
    }

    public void joinRanks(String puuid, List<RIOTRankInformationsEntity> newRanks, int seasonOrSet, String queue, GameEnum game) {
        if (!newRanks.isEmpty()) {
            RIOTRankEntity existingRanks = getRanks(puuid, game).orElse(new RIOTRankEntity(puuid));
            existingRanks.joinRanks(newRanks, seasonOrSet, queue);
            getRepository(game).save(existingRanks);
        }
    }

    public void updateRecentRanks(String puuid, GameEnum game) throws URISyntaxException, IOException {
        Optional<RIOTSummonerEntity> summoner = summonerRepository.findFirstByPuuid(puuid);
        if (summoner.isPresent()) {
            String stringUrl = String.format(getUrl(game), summoner.get().getId(), apiKeyRepository.findFirstByGame(game).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            for (JsonNode node : nodes) {
                RIOTRankInformationsEntity rank = mapper.convertValue(node, RIOTRankInformationsEntity.class);
                rank.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                joinRanks(puuid, List.of(rank), getCurrentSeasonOrSet(game), node.get("queueType").asText(), game);
            }
        }
    }

    private int getCurrentSeasonOrSet(GameEnum game) {
        RIOTMetadataEntity metadata = metadataService.getMetadata();
        return game == GameEnum.LOL ? metadata.getCurrentLOLSeason() : metadata.getCurrentTFTSet();
    }

    private String getUrl(GameEnum game) {
        return "https://euw1.api.riotgames.com/" + (game == GameEnum.LOL ? "lol/league/v4" : "tft/league/v1") + "/entries/by-summoner/%s?api_key=%s";
    }

    private RIOTRankRepository getRepository(GameEnum game) {
        return game == GameEnum.LOL ? lolRankRepository : tftRankRepository;
    }

}
