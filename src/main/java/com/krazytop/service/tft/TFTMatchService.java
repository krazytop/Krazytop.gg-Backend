package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.tft.TFTMatchRepository;
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
    private final ApiKeyRepository apiKeyRepository;
    private final RIOTSummonerService summonerService;

    @Autowired
    public TFTMatchService(TFTMatchRepository matchRepository, ApiKeyRepository apiKeyRepository, RIOTSummonerService summonerService) {
        this.matchRepository = matchRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.summonerService = summonerService;
    }

    public List<TFTMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, int set) {
        return this.getMatches(puuid, pageNb, TFTQueueEnum.fromName(queue), set);
    }

    public Long getLocalMatchesCount(String puuid, String queue, int set) {
        return this.getMatchesCount(puuid, TFTQueueEnum.fromName(queue), set);
    }

    private void updateMatch(String matchId, String puuid) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/%s?api_key=%s", matchId, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode infoNode = mapper.readTree(new URI(stringUrl).toURL()).get("info");
        TFTMatchEntity match = mapper.convertValue(infoNode, TFTMatchEntity.class);
        if (match.getQueue() != null && this.checkIfQueueIsCompatible(match)) {
            match.setId(matchId);
            match.getOwners().add(puuid);
            LOGGER.info("Saving TFT match : {}", matchId);
            matchRepository.save(match);
            summonerService.updateTimeSpentOnTFT(puuid, match.getDuration());
        }
    }

    public void updateRemoteToLocalMatches(String puuid, int firstIndex, boolean forceDetectNewMatches) throws IOException {
        List<TFTMatchEntity> matches = matchRepository.findAll();//TODO test set 11, 7, 7.5
        matches.stream().filter(m -> m.getSet() == 9).forEach(match -> {
            try {
                updateMatch(match.getId(), puuid);
                Thread.sleep(2000);
            } catch (URISyntaxException | InterruptedException | IOException e) {
            }
        });/**
        try {
            boolean moreMatchToRecovered = true;
            String stringUrl = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, firstIndex, 100, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(new URI(stringUrl).toURL());
            List<String> matchIds = mapper.convertValue(json, new TypeReference<>() {});
            if (!matchIds.isEmpty()) {
                for (String matchId : matchIds) {
                    TFTMatchEntity existingMatch = this.matchRepository.findFirstById(matchId);
                    if (existingMatch == null) {
                        this.updateMatch(matchId, puuid);
                        Thread.sleep(2000);
                    } else if (!existingMatch.getOwners().contains(puuid)) {
                        existingMatch.getOwners().add(puuid);
                        LOGGER.info("Updating TFT match : {}", matchId);
                        matchRepository.save(existingMatch);
                        summonerService.updateTimeSpentOnTFT(puuid, existingMatch.getDuration());
                    } else {
                        if (!forceDetectNewMatches) {
                            moreMatchToRecovered = false;
                            break;
                        }
                    }
                }
                if (moreMatchToRecovered) {
                    Thread.sleep(2000);
                    this.updateRemoteToLocalMatches(puuid, firstIndex + 100, forceDetectNewMatches);
                }
            }
        } catch (InterruptedException | URISyntaxException | IOException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }**/
    }

    private boolean checkIfQueueIsCompatible(TFTMatchEntity match) {
        List<String> compatibleQueues = Arrays.stream(TFTQueueEnum.class.getEnumConstants())
                .map(TFTQueueEnum::getIds)
                .flatMap(List::stream)
                .toList();
        return compatibleQueues.contains(match.getQueue().getId());
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
