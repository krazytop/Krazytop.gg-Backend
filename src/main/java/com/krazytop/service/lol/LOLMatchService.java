package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.LOLMatchDTO;
import com.krazytop.entity.lol.*;
import com.krazytop.exception.CustomException;
import com.krazytop.exception.ApiErrorEnum;
import com.krazytop.mapper.lol.LOLMatchMapper;
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
import java.util.List;
import java.util.Optional;

@Service
public class LOLMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchService.class);

    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;
    private final LOLMatchRepository matchRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final LOLSummonerService summonerService;
    private final LOLMatchMapper matchMapper;

    @Autowired
    public LOLMatchService(LOLMatchRepository matchRepository, ApiKeyRepository apiKeyRepository, LOLSummonerService summonerService, LOLMatchMapper matchMapper) {
        this.matchRepository = matchRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.summonerService = summonerService;
        this.matchMapper = matchMapper;
    }

    public List<LOLMatchDTO> getMatches(String puuid, Integer pageNb, String queue, String role) {
        return getMatches(puuid, pageNb, LOLQueueEnum.fromName(queue), LOLRoleEnum.fromName(role)).stream().map(matchMapper::toDTO).toList();
    }

    public Integer getMatchesCount(String puuid, String queue, String role) {
        return getMatchesCount(puuid, LOLQueueEnum.fromName(queue), LOLRoleEnum.fromName(role));
    }

    public void updateMatches(String puuid) {
        try {
            boolean moreMatchToRecovered = true;
            int firstIndex = 0;
            String apiKey = apiKeyRepository.findFirstByGame(GameEnum.LOL).getKey();
            while (moreMatchToRecovered) {// TODO faire un appel pour chaque region (europe, americas, asia, sea)
                String url = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, firstIndex, 100, apiKey);
                ObjectMapper mapper = new ObjectMapper();
                List<String> matchIds = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {
                });
                for (String matchId : matchIds) {
                    Optional<LOLMatch> existingMatch = this.matchRepository.findFirstById(matchId);
                    if (existingMatch.isEmpty()) {
                        String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s", matchId, apiKey);
                        JsonNode node = mapper.readTree(new URI(stringUrl).toURL());
                        LOLMatch match = mapper.convertValue(node.get("info"), LOLMatch.class);
                        match.setId(node.get("metadata").get("matchId").asText());
                        if (LOLQueueEnum.ARENA.getIds().contains(match.getQueue())) {
                            match.dispatchParticipantsInTeamsArena();
                        } else {
                            match.dispatchParticipantsInTeamsNormalGame();
                            match.setRemake(match.getTeams().getFirst().getParticipants().getFirst().getGameEndedInEarlySurrender());
                        }
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
        } catch (IOException | URISyntaxException | InterruptedException ex) {
            throw new CustomException(ApiErrorEnum.MATCH_UPDATE_ERROR, ex);
        }
    }

    private void saveMatch(LOLMatch match, String puuid) {
        match.getOwners().add(puuid);
        LOGGER.info("Saving LOL match : {}", match.getId());
        matchRepository.save(match);
        summonerService.updateSpentTimeAndPlayedSeasonsOrSets(puuid, match.getDuration(), Integer.valueOf(match.getVersion().replaceAll("\\..*", "")));
    }

    private List<LOLMatch> getMatches(String puuid, int pageNb, LOLQueueEnum queue, LOLRoleEnum role) {
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

    private Integer getMatchesCount(String puuid, LOLQueueEnum queue, LOLRoleEnum role) {
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

}
