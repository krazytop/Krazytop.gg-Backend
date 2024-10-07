package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.lol.*;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.nomenclature.lol.LOLQueueEnum;
import com.krazytop.nomenclature.lol.LOLRoleEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLMatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Service
public class LOLMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchService.class);

    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;
    private final LOLMatchRepository matchRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public LOLMatchService(LOLMatchRepository matchRepository, ApiKeyRepository apiKeyRepository) {
        this.matchRepository = matchRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<LOLMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, String role) {
        return this.getMatches(puuid, pageNb, LOLQueueEnum.fromName(queue), LOLRoleEnum.fromName(role));
    }

    public Long getLocalMatchesCount(String puuid, String queue, String role) {
        return this.getMatchesCount(puuid, LOLQueueEnum.fromName(queue), LOLRoleEnum.fromName(role));
    }

    private void updateMatch(String matchId, String puuid) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s", matchId, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode infoNode = mapper.readTree(new URI(stringUrl).toURL()).get("info");
        LOLMatchEntity match = mapper.convertValue(infoNode, LOLMatchEntity.class);
        match.setId(matchId);
        match.getOwners().add(puuid);
        if (match.isQueue(LOLQueueEnum.ARENA)) {
            match.buildArenaMatch();
        } else {
            match.dispatchParticipantsInTeamsAndBuildSummoners();
            match.setRemake(match.getTeams().get(0).getParticipants().get(0).isGameEndedInEarlySurrender());
        }
        if (this.checkIfQueueIsCompatible(match)) {
            LOGGER.info("Saving match : {}", matchId);
            matchRepository.save(match);
        }
    }

    /**
     * Due to development API Key rate limit, we recover only and always 100 last matches
     */
    public void updateRemoteToLocalMatches(String puuid) throws IOException {
        try {
            String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, 0, 100, apiKeyRepository.findFirstByGame(GameEnum.RIOT).getKey());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(new URI(stringUrl).toURL());
            List<String> matchIds = mapper.convertValue(json, new TypeReference<>() {});
            for (String matchId : matchIds) {
                LOLMatchEntity existingMatch = this.matchRepository.findFirstById(matchId);
                if (existingMatch == null) {
                    this.updateMatch(matchId, puuid);
                    Thread.sleep(2000);
                } else if (!existingMatch.getOwners().contains(puuid)) {
                    existingMatch.getOwners().add(puuid);
                    LOGGER.info("Updating match : {}", matchId);
                    matchRepository.save(existingMatch);
                } else {
                    break;
                }
            }
        } catch (InterruptedException | URISyntaxException | IOException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }

    private boolean checkIfQueueIsCompatible(LOLMatchEntity match) {
        List<String> compatibleQueues = Arrays.stream(LOLQueueEnum.class.getEnumConstants())
                .map(LOLQueueEnum::getIds)
                .flatMap(List::stream)
                .toList();
        return compatibleQueues.contains(match.getQueue().getId());
    }

    public List<LOLMatchEntity> getMatches(String puuid, int pageNb, LOLQueueEnum queue, LOLRoleEnum role) {
        PageRequest pageRequest = PageRequest.of(pageNb, pageSize);
        if (queue == LOLQueueEnum.ALL_QUEUES) {
            if (role == LOLRoleEnum.ALL_ROLES) {
                return this.matchRepository.findAll(puuid, pageRequest).getContent();
            } else {
                return this.matchRepository.findAllByRole(puuid, role.getRiotName(), pageRequest).getContent();
            }
        } else {
            if (role == LOLRoleEnum.ALL_ROLES) {
                return this.matchRepository.findAllByQueue(puuid, queue.getIds(), pageRequest).getContent();
            } else {
                return this.matchRepository.findAllByQueueAndByRole(puuid, queue.getIds(), role.getRiotName(), pageRequest).getContent();
            }
        }
    }

    public Long getMatchesCount(String puuid, LOLQueueEnum queue, LOLRoleEnum role) {
        if (queue == LOLQueueEnum.ALL_QUEUES) {
            if (role == LOLRoleEnum.ALL_ROLES) {
                return this.matchRepository.countAll(puuid);
            } else {
                return this.matchRepository.countAllByRole(puuid, role.getRiotName());
            }
        } else {
            if (role == LOLRoleEnum.ALL_ROLES) {
                return this.matchRepository.countAllByQueue(puuid, queue.getIds());
            } else {
                return this.matchRepository.countAllByQueueAndByRole(puuid, queue.getIds(), role.getRiotName());
            }
        }
    }

    private String getRole(String roleName) {
        return switch (roleName) {
            case "top" -> "TOP";
            case "jungle" -> "JUNGLE";
            case "middle" -> "MIDDLE";
            case "bottom" -> "BOTTOM";
            case "support" -> "UTILITIES";
            default -> "";
        };
    }

}
