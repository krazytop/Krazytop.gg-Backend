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
import com.krazytop.repository.riot.RIOTSummonerRepository;
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
    private final RIOTSummonerRepository summonerRepository;
    private final RIOTMetadataRepository metadataRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final RIOTSummonerService summonerService;

    @Autowired
    public TFTMatchService(TFTMatchRepository matchRepository, RIOTSummonerRepository summonerRepository, RIOTMetadataRepository metadataRepository, ApiKeyRepository apiKeyRepository, RIOTSummonerService summonerService) {
        this.matchRepository = matchRepository;
        this.summonerRepository = summonerRepository;
        this.metadataRepository = metadataRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.summonerService = summonerService;
    }

    public List<TFTMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, int set) {
        return this.getMatches(puuid, pageNb, TFTQueueEnum.fromName(queue), set);
    }

    public Long getLocalMatchesCount(String puuid, String queue, int set) {
        return this.getMatchesCount(puuid, TFTQueueEnum.fromName(queue), set);
    }

    public void updateAllMatches(String puuid) throws IOException, URISyntaxException {
        if (getLocalMatchesCount(puuid, TFTQueueEnum.ALL_QUEUES.getName(), -1) == 0) {
            LOGGER.info("Updating TFT legacy matches");
            updateLegacyMatchesFromLOLChess(puuid);
        }
        LOGGER.info("Updating TFT recent matches");
        updateRecentMatches(puuid);
    }

    public void updateRecentMatches(String puuid) throws IOException {
        try {
            boolean moreMatchToRecovered = true;
            int firstIndex = 0;
            while (moreMatchToRecovered) {
                String url = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, firstIndex, 100, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
                ObjectMapper mapper = new ObjectMapper();
                List<String> matchIds = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
                for (String matchId : matchIds) {
                    TFTMatchEntity existingMatch = this.matchRepository.findFirstById(matchId);
                    if (existingMatch == null) {
                        updateMatch(matchId, puuid);
                        Thread.sleep(2000);
                    } else if (!existingMatch.getOwners().contains(puuid)) {
                        saveMatch(existingMatch, puuid);
                    } else {
                        moreMatchToRecovered = false;
                        break;
                    }
                }
                Thread.sleep(2000);
                firstIndex += 100;
            }
        } catch (InterruptedException | URISyntaxException | IOException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    private void updateLegacyMatchesFromLOLChess(String puuid) throws URISyntaxException, IOException {
        Optional<RIOTMetadataEntity> metadata = metadataRepository.findFirstByOrderByIdAsc();
        RIOTSummonerEntity summoner = summonerService.getRemoteSummonerByPuuid(puuid);
        int latestSet = metadata.map(RIOTMetadataEntity::getCurrentTFTSet).orElse(0);
        int setNb = 1;
        while (setNb <= latestSet) {
            int pageNb = 1;
            boolean lastPage = false;
            while (!lastPage) {
                String url = String.format("https://tft.dakgg.io/api/v1/summoners/euw1/%s-%s/matches?season=set%s&page=%d", summoner.getName(), summoner.getTag(), setNb, pageNb);
                ObjectMapper mapper = new ObjectMapper();
                List<TFTMatchEntity> matches = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("matches"), new TypeReference<>() {});
                matches.forEach(match -> saveMatch(match, puuid));
                lastPage = matches.isEmpty();
                pageNb ++;
            }
            setNb ++;
        }
    }

    private void updateMatch(String matchId, String puuid) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/%s?api_key=%s", matchId, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
        ObjectMapper mapper = new ObjectMapper();
        TFTMatchEntity match = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()).get("info"), TFTMatchEntity.class);
        saveMatch(match, puuid);
    }

    private void saveMatch(TFTMatchEntity match, String puuid) {
        if (match.getQueue() != null && this.checkIfQueueIsCompatible(match) || true) {
            match.setId("EUW1_" + match.getId());
            match.getOwners().add(puuid);
            LOGGER.info("Saving TFT match : {}", match.getId());
            matchRepository.save(match);
            summonerService.updateTimeSpentOnTFT(puuid, match.getDuration());
        }
    }

    private boolean checkIfQueueIsCompatible(TFTMatchEntity match) {
        List<Integer> compatibleQueues = Arrays.stream(TFTQueueEnum.class.getEnumConstants())
                .map(TFTQueueEnum::getIds)
                .flatMap(List::stream)
                .toList();
        return compatibleQueues.contains(match.getQueue());
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
