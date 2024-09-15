package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api.lol.LOLMatchApi;
import com.krazytop.entity.lol.*;
import com.krazytop.repository.lol.*;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class LOLMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchService.class);

    private final LOLMatchApi matchApi;
    private final LOLMatchRepository matchRepository;
    private final RIOTApiKeyRepository apiKeyRepository;

    @Autowired
    public LOLMatchService(LOLMatchApi matchApi, LOLMatchRepository matchRepository, RIOTApiKeyRepository apiKeyRepository) {
        this.matchApi = matchApi;
        this.matchRepository = matchRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<LOLMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, String role) {
        return matchApi.getMatches(puuid, pageNb, queue, role);
    }

    public long getLocalMatchesCount(String puuid, String queue, String role) {
        return matchApi.getMatchCount(puuid, queue, role);
    }

    private void updateMatch(String matchId) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s", matchId, apiKeyRepository.findFirstByOrderByKeyAsc());
        JsonNode infoNode = new ObjectMapper().readTree(new URI(stringUrl).toURL()).get("info");
        LOLMatchEntity match = new ObjectMapper().convertValue(infoNode, LOLMatchEntity.class);
        match.setId(matchId);
        match.dispatchParticipantsInTeamsAndBuildSummoners();
        match.setRemake(match.getTeams().get(0).getParticipants().get(0).isGameEndedInEarlySurrender());
        if (this.checkIfQueueIsCompatible(match)) {
            LOGGER.info("Saving match : {}", matchId);
            matchRepository.save(match);
            LOLMatchEntity m = matchRepository.findFirstById(matchId);
            LOLMatchEntity p = matchRepository.findFirstById(matchId);
        }
    }

    /**
     * Due to development API Key rate limit, we recover only and always 100 last matches
     */
    public void updateRemoteToLocalMatches(String puuid) {

        try {
            //TODO count à 1 pour les tests
            String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, 0, 1, apiKeyRepository.findFirstByOrderByKeyAsc());
            ObjectMapper mapper = new ObjectMapper();
            List<String> matchIds = mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});

            for (String matchId : matchIds) {
                if (this.matchRepository.findFirstById(matchId) != null) {
                    break;
                }
                this.updateMatch(matchId);
                Thread.sleep(2000);
            }
        } catch (InterruptedException | URISyntaxException | IOException e) {
            LOGGER.error("Error while updating matches : {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private boolean checkIfQueueIsCompatible(LOLMatchEntity match) {
        List<String> compatibleQueues = List.of("325", "400", "420", "430", "440", "450", "490", "700", "720", "900", "1020", "1900");
        return compatibleQueues.contains(match.getQueue().getId());
    }


}
