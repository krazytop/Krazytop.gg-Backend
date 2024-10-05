package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.lol.LOLRankEntity;
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

@Service
public class LOLRankService {

    private final ApiKeyRepository apiKeyRepository;
    private final LOLRankRepository rankRepository;

    @Autowired
    public LOLRankService(ApiKeyRepository apiKeyRepository, LOLRankRepository rankRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.rankRepository = rankRepository;
    }

    public LOLRankEntity getLocalRank(String summonerId, String queueType) {
        return rankRepository.findFirstBySummonerIdAndQueueOrderByUpdateDateDesc(summonerId, queueType);
    }

    public void updateRemoteToLocalRank(String summonerId) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s", summonerId, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
        ObjectMapper mapper = new ObjectMapper();
        List<LOLRankEntity> ranks = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
        List<String> compatiblesRanks = List.of("RANKED_SOLO_5x5", "RANKED_TEAM_5x5");
        ranks = ranks.stream()
                .filter(rank -> compatiblesRanks.contains(rank.getQueue()))
                .filter(rank -> rank.needToUpdate(getLocalRank(summonerId, rank.getQueue())))
                .toList();
        ranks.forEach(rank -> rank.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
        rankRepository.saveAll(ranks);

    }

}
