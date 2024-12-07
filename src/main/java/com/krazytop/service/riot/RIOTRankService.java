package com.krazytop.service.riot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTRankEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RIOTRankService {

    private final ApiKeyRepository apiKeyRepository;
    private final RIOTSummonerRepository summonerRepository;

    @Autowired
    public RIOTRankService(ApiKeyRepository apiKeyRepository, RIOTSummonerRepository summonerRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.summonerRepository = summonerRepository;
    }

    public void updateRemoteToLocalRank(String puuid, String url, int currentSetOrSeason, RIOTRankRepository repository) throws URISyntaxException, IOException {
        RIOTSummonerEntity summoner = summonerRepository.findFirstByPuuid(puuid);
        if (summoner != null) {
            String stringUrl = String.format(url, summoner.getId(), apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
            ObjectMapper mapper = new ObjectMapper();
            RIOTRankEntity newRanks = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            RIOTRankEntity existingRanks = repository.findFirstByPuuid(puuid);
            if (existingRanks == null) {
                existingRanks = new RIOTRankEntity(puuid, new HashMap<>());
            }
            existingRanks.getRanks().computeIfAbsent(currentSetOrSeason, k -> new HashMap<>());
            Map<String, List<RIOTRankEntity.RankInformations>> currentSetLatestRanks = newRanks.getRanks().get(-1);
            Map<String, List<RIOTRankEntity.RankInformations>> currentSetRanks = existingRanks.getRanks().get(currentSetOrSeason);
            currentSetLatestRanks.forEach((queue, rank) -> {
                currentSetRanks.computeIfAbsent(queue, k -> new ArrayList<>());
                List<RIOTRankEntity.RankInformations> currentQueueRank = currentSetRanks.get(queue);
                if (currentQueueRank.isEmpty() || currentQueueRank.get(currentQueueRank.size() - 1).needUpdating(rank.get(0))) {
                    currentQueueRank.add(rank.get(0));
                }
            });
            repository.save(existingRanks);
        }
    }

}
