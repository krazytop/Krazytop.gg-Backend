package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.lol.LOLMasteriesEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLMasteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class LOLMasteryService {

    private final ApiKeyRepository apiKeyRepository;
    private final LOLMasteryRepository masteryRepository;

    @Autowired
    public LOLMasteryService(ApiKeyRepository apiKeyRepository, LOLMasteryRepository masteryRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.masteryRepository = masteryRepository;
    }

    public Optional<LOLMasteriesEntity> getMasteries(String puuid) {
        return masteryRepository.findByPuuid(puuid);
    }

    public void updateMasteries(String region, String puuid) {
        try {
            String stringUrl = String.format("https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/%s?api_key=%s", puuid, apiKeyRepository.findFirstByGame(GameEnum.LOL).getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            LOLMasteriesEntity masteries = new LOLMasteriesEntity(nodes.get(0).get("puuid").asText());
            nodes.forEach(node -> masteries.getChampions().add(mapper.convertValue(node, LOLMasteriesEntity.LOLMasteryEntity.class)));
            masteryRepository.save(masteries);
        } catch (IOException | URISyntaxException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.MASTERIES_CANT_BE_UPDATED);
        }
    }

}
