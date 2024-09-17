package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLRankService.class);

    private final RIOTApiKeyRepository apiKeyRepository;
    private final LOLRankRepository rankRepository;

    @Autowired
    public LOLRankService(RIOTApiKeyRepository apiKeyRepository, LOLRankRepository rankRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.rankRepository = rankRepository;
    }

    public LOLRankEntity getLocalRank(String summonerId, String queueType) {
        return rankRepository.findFirstBySummonerIdAndQueueOrderByUpdateDateDesc(summonerId, queueType);
    }

    public List<LOLRankEntity> updateRemoteToLocalRank(String summonerId) {
        try {
            String stringUrl = String.format("https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/%s?api_key=%s", summonerId, apiKeyRepository.findFirstByOrderByKeyAsc().getKey());
            ObjectMapper mapper = new ObjectMapper();
            List<LOLRankEntity> ranks = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
            List<String> compatiblesRanks = List.of("RANKED_SOLO_5x5", "RANKED_TEAM_5x5");
            ranks = ranks.stream()
                    .filter(rank -> compatiblesRanks.contains(rank.getQueue()))
                    .filter(rank -> !rank.equals(getLocalRank(summonerId, rank.getQueue())))
                    .toList();
            ranks.forEach(rank -> rank.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
            return rankRepository.saveAll(ranks);
        } catch (URISyntaxException | IOException e) {
            LOGGER.error("An error occurred while updating ranks : {}", e.getMessage());
            return List.of();
        }

    }

}
