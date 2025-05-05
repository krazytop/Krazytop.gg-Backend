package com.krazytop.service.riot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.entity.riot.rank.RIOTRankInformationsEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
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
    private final TFTRankRepository tftRankRepository;
    private final LOLRankRepository lolRankRepository;
    private final RIOTMetadataService metadataService;

    @Autowired
    public RIOTRankService(ApiKeyRepository apiKeyRepository, TFTRankRepository tftRankRepository, LOLRankRepository lolRankRepository, RIOTMetadataService metadataService) {
        this.apiKeyRepository = apiKeyRepository;
        this.tftRankRepository = tftRankRepository;
        this.lolRankRepository = lolRankRepository;
        this.metadataService = metadataService;
    }

    public Optional<RIOTRankEntity> getRanks(String summonerId, GameEnum game) {
        return getRepository(game).findBySummonerId(summonerId);
    }

    public void updateRanks(String region, String summonerId, GameEnum game) {
        try {
            int currentSeasonOrSet = getCurrentSeasonOrSet(game);
            String stringUrl = String.format(getUrl(region, game), summonerId, apiKeyRepository.findFirstByGame(game).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            for (JsonNode node : nodes) {
                RIOTRankInformationsEntity rank = mapper.convertValue(node, RIOTRankInformationsEntity.class);
                rank.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                joinRanks(summonerId, List.of(rank), currentSeasonOrSet, node.get("queueType").asText(), game);//TODO je ne sais plus quoi
            }
        } catch (URISyntaxException | IOException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.RANKS_CANT_BE_UPDATED);
        }
    }

    public void joinRanks(String summonerId, List<RIOTRankInformationsEntity> newRanks, int seasonOrSet, String queue, GameEnum game) {
        if (!newRanks.isEmpty()) {
            RIOTRankEntity existingRanks = getRanks(summonerId, game).orElse(new RIOTRankEntity(summonerId));
            existingRanks.joinRanks(newRanks, seasonOrSet, queue);
            getRepository(game).save(existingRanks);
        }
    }

    private int getCurrentSeasonOrSet(GameEnum game) {
        RIOTMetadataEntity metadata = metadataService.getMetadata();
        return game == GameEnum.LOL ? metadata.getCurrentLOLSeason() : metadata.getCurrentTFTSet();
    }

    private String getUrl(String region, GameEnum game) {
        return "https://euw1.api.riotgames.com/" + (game == GameEnum.LOL ? "lol/league/v4" : "tft/league/v1") + "/entries/by-summoner/%s?api_key=%s";
    }

    private RIOTRankRepository getRepository(GameEnum game) {
        return game == GameEnum.LOL ? lolRankRepository : tftRankRepository;
    }

}
