package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.lol.LOLMasteryEntity;
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

    @Autowired
    public LOLMasteryService(ApiKeyRepository apiKeyRepository, LOLMasteryRepository masteryRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.masteryRepository = masteryRepository;
    }

    public List<LOLMasteryEntity> getLocalMasteries(String puuid) {
        return masteryRepository.findAllByPuuid(puuid);
    }

    public void updateRemoteToLocalMasteries(String puuid) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/%s?api_key=%s", puuid, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
        ObjectMapper mapper = new ObjectMapper();
        List<LOLMasteryEntity> masteries = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
        masteryRepository.deleteAllByPuuid(puuid);
        masteryRepository.saveAll(masteries);
    }

}
