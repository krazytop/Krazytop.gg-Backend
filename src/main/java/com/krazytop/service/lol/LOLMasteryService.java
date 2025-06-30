package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.LOLMasteriesDTO;
import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.lol.LOLMasteries;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.mapper.lol.LOLMasteryMapper;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLMasteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class LOLMasteryService {

    private final ApiKeyRepository apiKeyRepository;
    private final LOLMasteryRepository masteryRepository;
    private final LOLSummonerService summonerService;
    private final LOLMasteryMapper masteryMapper;

    @Autowired
    public LOLMasteryService(ApiKeyRepository apiKeyRepository, LOLMasteryRepository masteryRepository, LOLSummonerService summonerService, LOLMasteryMapper masteryMapper) {
        this.apiKeyRepository = apiKeyRepository;
        this.masteryRepository = masteryRepository;
        this.summonerService = summonerService;
        this.masteryMapper = masteryMapper;
    }

    public LOLMasteries getMasteries(String puuid) {
        return masteryRepository.findByPuuid(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.SUMMONER_NEED_IMPORT_FIRST));
    }

    public LOLMasteriesDTO getMasteriesDTO(String puuid) {
        return masteryMapper.toDTO(getMasteries(puuid));
    }

    public void updateMasteries(String puuid) {
        try {
            String region = summonerService.getLocalSummoner(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.SUMMONER_NEED_IMPORT_FIRST)).getRegion();
            String stringUrl = String.format("https://%s.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/%s?api_key=%s", region, puuid, apiKeyRepository.findFirstByGame(GameEnum.LOL).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            LOLMasteries masteries = new LOLMasteries(nodes.get(0).get("puuid").asText());
            nodes.forEach(node -> masteries.getChampions().add(mapper.convertValue(node, LOLMasteries.LOLMastery.class)));
            masteryRepository.save(masteries);
        } catch (IOException | URISyntaxException ex) {
            throw new CustomException(ApiErrorEnum.MASTERY_UPDATE_ERROR, ex);
        }
    }

}
