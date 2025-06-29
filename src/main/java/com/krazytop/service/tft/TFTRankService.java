package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.RIOTRankDTO;
import com.krazytop.entity.riot.rank.RIOTRank;
import com.krazytop.entity.riot.rank.RIOTRankInformationsEntity;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.mapper.tft.TFTRankMapper;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.tft.TFTRankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TFTRankService {

    private final ApiKeyRepository apiKeyRepository;
    private final TFTRankRepository rankRepository;
    private final TFTMetadataService metadataService;
    private final TFTSummonerService summonerService;
    private final TFTRankMapper rankMapper;

    @Autowired
    public TFTRankService(ApiKeyRepository apiKeyRepository, TFTRankRepository rankRepository, TFTMetadataService metadataService, TFTSummonerService summonerService, TFTRankMapper rankMapper) {
        this.apiKeyRepository = apiKeyRepository;
        this.rankRepository = rankRepository;
        this.metadataService = metadataService;
        this.summonerService = summonerService;
        this.rankMapper = rankMapper;
    }

    public Optional<RIOTRank> getRanks(String puuid) {
        return rankRepository.findByPuuid(puuid);
    }

    public RIOTRankDTO getRanksDTO(String puuid) {
        return rankMapper.toDTO(getRanks(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.RANK_NEED_IMPORT_FIRST)));
    }

    public void updateRanks(String puuid) {
        try {
            int currentSeasonOrSet = metadataService.getMetadataDTO().getCurrentSeasonOrSet();
            String region = summonerService.getLocalSummoner(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.RANK_NEED_IMPORT_FIRST)).getRegion();
            String url = String.format("https://%s.api.riotgames.com/tft/league/v1/entries/by-puuid/%s?api_key=%s", region, puuid, apiKeyRepository.findFirstByGame(GameEnum.TFT).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
            RIOTRank rank = getRanks(puuid).orElse(new RIOTRank(puuid));
            for (JsonNode node : nodes) {
                RIOTRankInformationsEntity rankInformations = mapper.convertValue(node, RIOTRankInformationsEntity.class);
                rankInformations.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                rank.joinRanks(List.of(rankInformations), currentSeasonOrSet, node.get("queueType").asText());//TODO je ne sais plus quoi, mettre la fonction dans la class rank
            }
            rankRepository.save(rank);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.SUMMONER_NOT_FOUND, ex);
        }
    }
}
