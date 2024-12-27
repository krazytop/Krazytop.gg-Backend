package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import com.krazytop.repository.tft.TFTMatchRepository;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTSummonerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class TFTMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTMatchService.class);

    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;
    private final TFTMatchRepository matchRepository;
    private final RIOTMetadataService metadataService;
    private final ApiKeyRepository apiKeyRepository;
    private final RIOTSummonerService summonerService;

    @Autowired
    public TFTMatchService(TFTMatchRepository matchRepository, RIOTMetadataService metadataService, ApiKeyRepository apiKeyRepository, RIOTSummonerService summonerService) {
        this.matchRepository = matchRepository;
        this.metadataService = metadataService;
        this.apiKeyRepository = apiKeyRepository;
        this.summonerService = summonerService;
    }

    public List<TFTMatchEntity> getMatches(String puuid, int pageNb, String queue, int set) {
        return getMatches(puuid, pageNb, TFTQueueEnum.fromName(queue), set);
    }

    public Long getMatchesCount(String puuid, String queue, int set) {
        return getMatchesCount(puuid, TFTQueueEnum.fromName(queue), set);
    }

    public void updateMatches(String puuid) throws IOException, URISyntaxException, InterruptedException {
        if (getMatchesCount(puuid, TFTQueueEnum.ALL_QUEUES, -1) == 0) {
            updateLegacyMatchesFromLOLChess(puuid);
        }
        updateRecentMatches(puuid);
    }

    private void updateRecentMatches(String puuid) throws IOException, InterruptedException, URISyntaxException {
        boolean moreMatchToRecovered = true;
        int firstIndex = 0;
        String apiKey = apiKeyRepository.findFirstByGame(GameEnum.TFT).getKey();
        while (moreMatchToRecovered) {
            String url = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, firstIndex, 100, apiKey);
            ObjectMapper mapper = new ObjectMapper();
            List<String> matchIds = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
            for (String matchId : matchIds) {
                Optional<TFTMatchEntity> existingMatch = this.matchRepository.findFirstById(matchId);
                if (existingMatch.isEmpty()) {
                    String stringUrl = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/%s?api_key=%s", matchId, apiKey);
                    JsonNode node = mapper.readTree(new URI(stringUrl).toURL());
                    TFTMatchEntity match = mapper.convertValue(node.get("info"), TFTMatchEntity.class);
                    match.setId(node.get("metadate").get("match_id").asText());
                    saveMatch(match, puuid);
                    Thread.sleep(2000);
                } else if (!existingMatch.get().getOwners().contains(puuid)) {
                    saveMatch(existingMatch.get(), puuid);
                } else {
                    moreMatchToRecovered = false;
                    break;
                }
            }
            Thread.sleep(2000);
            firstIndex += 100;
        }
    }

    private void updateLegacyMatchesFromLOLChess(String puuid) throws URISyntaxException, IOException {
        LOGGER.info("Updating legacy TFT matches from LOLChess");
        RIOTMetadataEntity metadata = metadataService.getMetadata();
        RIOTSummonerEntity summoner = summonerService.getRemoteSummonerByPuuid(puuid);
        int latestSet = metadata.getCurrentTFTSet();
        int setNb = 1;
        while (setNb <= latestSet) {
            int pageNb = 1;
            boolean lastPage = false;
            while (!lastPage) {
                String url = String.format("https://tft.dakgg.io/api/v1/summoners/euw1/%s-%s/matches?season=set%s&page=%d", summoner.getName(), summoner.getTag(), setNb, pageNb);
                ObjectMapper mapper = new ObjectMapper();
                List<TFTMatchEntity> matches = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("matches"), new TypeReference<>() {});
                matches.forEach(match -> {
                    Optional<TFTMatchEntity> existingMatch = matchRepository.findFirstById("EUW1_" + match.getId());
                    if (existingMatch.isEmpty() || !existingMatch.get().getOwners().contains(puuid)) {
                        saveMatch(match, puuid);
                    }
                });
                lastPage = matches.isEmpty();
                pageNb ++;
            }
            setNb ++;
        }
    }

    private void saveMatch(TFTMatchEntity match, String puuid) {
        match.getOwners().add(puuid);
        LOGGER.info("Saving TFT match : {}", match.getId());
        matchRepository.save(match);
        summonerService.updateTimeSpentOnTFT(puuid, match.getDuration());
    }

    public List<TFTMatchEntity> getMatches(String puuid, int pageNb, TFTQueueEnum queue, int set) {
        PageRequest pageRequest = PageRequest.of(pageNb, pageSize);
        if (queue == TFTQueueEnum.ALL_QUEUES) {
            if (set == -1) {
                return this.matchRepository.findAll(puuid, pageRequest).getContent();
            } else {
                return this.matchRepository.findAllBySet(puuid, set, pageRequest).getContent();
            }
        } else {
            if (set == -1) {
                return this.matchRepository.findAllByQueue(puuid, queue.getIds(), pageRequest).getContent();
            } else {
                return this.matchRepository.findAllByQueueAndBySet(puuid, queue.getIds(), set, pageRequest).getContent();
            }
        }
    }

    public Long getMatchesCount(String puuid, TFTQueueEnum queue, int set) {
        if (queue == TFTQueueEnum.ALL_QUEUES) {
            if (set == -1) {
                System.out.println(matchRepository.countAll(puuid));
                return this.matchRepository.countAll(puuid);
            } else {
                return this.matchRepository.countAllBySet(puuid, set);
            }
        } else {
            if (set == -1) {
                return this.matchRepository.countAllByQueue(puuid, queue.getIds());
            } else {
                return this.matchRepository.countAllByQueueAndBySet(puuid, queue.getIds(), set);
            }
        }
    }

}
