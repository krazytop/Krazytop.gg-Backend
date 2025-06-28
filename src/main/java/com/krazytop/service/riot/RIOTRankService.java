package com.krazytop.service.riot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.entity.riot.rank.RIOTRankInformationsEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.tft.TFTRankRepository;
import com.krazytop.service.lol.LOLMetadataService;
import com.krazytop.service.tft.TFTMetadataService;
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
    private final TFTRankRepository tftRankRepository;
    private final LOLRankRepository lolRankRepository;
    private final LOLMetadataService lolMetadataService;
    private final TFTMetadataService tftMetadataService;

    @Autowired
    public RIOTRankService(ApiKeyRepository apiKeyRepository, TFTRankRepository tftRankRepository, LOLRankRepository lolRankRepository, LOLMetadataService lolMetadataService, TFTMetadataService tftMetadataService) {
        this.apiKeyRepository = apiKeyRepository;
        this.tftRankRepository = tftRankRepository;
        this.lolRankRepository = lolRankRepository;
        this.lolMetadataService = lolMetadataService;
        this.tftMetadataService = tftMetadataService;
    }

    public Optional<RIOTRankEntity> getRanks(String puuid, GameEnum game) {
        return getRepository(game).findByPuuid(puuid);
    }

    public void updateRanks(String region, String puuid, GameEnum game) {
        try {
            int currentSeasonOrSet = getCurrentSeasonOrSet(game);
            String stringUrl = String.format(getUrl(region, game), puuid, apiKeyRepository.findFirstByGame(game).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            for (JsonNode node : nodes) {
                RIOTRankInformationsEntity rank = mapper.convertValue(node, RIOTRankInformationsEntity.class);
                rank.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                joinRanks(puuid, List.of(rank), currentSeasonOrSet, node.get("queueType").asText(), game);//TODO je ne sais plus quoi
            }
        } catch (URISyntaxException | IOException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.RANKS_CANT_BE_UPDATED);
        }
    }

    public void joinRanks(String puuid, List<RIOTRankInformationsEntity> newRanks, int seasonOrSet, String queue, GameEnum game) {
        if (!newRanks.isEmpty()) {
            RIOTRankEntity existingRanks = getRanks(puuid, game).orElse(new RIOTRankEntity(puuid));
            existingRanks.joinRanks(newRanks, seasonOrSet, queue);
            getRepository(game).save(existingRanks);
        }
    }

    private int getCurrentSeasonOrSet(GameEnum game) {
        return game == GameEnum.LOL ? lolMetadataService.getMetadataDTO().getCurrentSeasonOrSet()
                : tftMetadataService.getMetadataDTO().getCurrentSeasonOrSet();
    }

    private String getUrl(String region, GameEnum game) {
        return "https://euw1.api.riotgames.com/" + (game == GameEnum.LOL ? "lol/league/v4" : "tft/league/v1") + "/entries/by-puuid/%s?api_key=%s";
    }

    private RIOTRankRepository getRepository(GameEnum game) {
        return game == GameEnum.LOL ? lolRankRepository : tftRankRepository;
    }

}
