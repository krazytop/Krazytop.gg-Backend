package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.RIOTRankDTO;
import com.krazytop.entity.riot.rank.RIOTRank;
import com.krazytop.entity.riot.rank.RIOTRankInformations;
import com.krazytop.exception.CustomException;
import com.krazytop.exception.ApiErrorEnum;
import com.krazytop.mapper.lol.LOLRankMapper;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLRankRepository;
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
public class LOLRankService {

    private final ApiKeyRepository apiKeyRepository;
    private final LOLRankRepository rankRepository;
    private final LOLMetadataService metadataService;
    private final LOLSummonerService summonerService;
    private final LOLRankMapper rankMapper;

    @Autowired
    public LOLRankService(ApiKeyRepository apiKeyRepository, LOLRankRepository rankRepository, LOLMetadataService metadataService, LOLSummonerService summonerService, LOLRankMapper rankMapper) {
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
        return rankMapper.toDTO(getRanks(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.SUMMONER_NEED_IMPORT_FIRST)));
    }

    public void updateRanks(String puuid) {
        try {
            int currentSeasonOrSet = metadataService.getMetadataDTO().getCurrentSeasonOrSet();
            String region = summonerService.getLocalSummoner(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.SUMMONER_NEED_IMPORT_FIRST)).getRegion();
            String url = String.format("https://%s.api.riotgames.com/lol/league/v4/entries/by-puuid/%s?api_key=%s", region, puuid, apiKeyRepository.findFirstByGame(GameEnum.LOL).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
            RIOTRank rank = getRanks(puuid).orElse(new RIOTRank(puuid));
            for (JsonNode node : nodes) {
                RIOTRankInformations rankInformations = mapper.convertValue(node, RIOTRankInformations.class);
                rankInformations.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                rank.joinRanks(List.of(rankInformations), currentSeasonOrSet, node.get("queueType").asText());//TODO je ne sais plus quoi, mettre la fonction dans la class rank
            }
            rankRepository.save(rank);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.RANK_UPDATE_ERROR, ex);
        }
    }
}
