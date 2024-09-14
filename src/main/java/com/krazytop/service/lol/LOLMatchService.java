package com.krazytop.service.lol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api.lol.LOLMatchApi;
import com.krazytop.entity.lol.*;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_response.lol.LOLMatchHTTPResponse;
import com.krazytop.http_response.lol.LOLMatchIdsHTTPResponse;
import com.krazytop.http_response.lol.LOLParticipantHTTPResponse;
import com.krazytop.http_response.lol.LOLTeamHTTPResponse;
import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import com.krazytop.repository.lol.*;
import com.krazytop.repository.riot.RIOTApiKeyRepository;
import com.krazytop.service.riot.RIOTApiService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class LOLMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchService.class);

    private final LOLMatchApi matchApi;
    private final RIOTApiService riotApiService;
    private final LOLChampionNomenclatureRepository championNomenclatureRepository;
    private final LOLItemNomenclatureRepository itemNomenclatureRepository;
    private final LOLRuneNomenclatureRepository runeNomenclatureRepository;
    private final LOLQueueNomenclatureRepository queueNomenclatureRepository;
    private final LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository;
    private final LOLMatchRepository matchRepository;
    private final RIOTApiKeyRepository apiKeyRepository;

    @Autowired
    public LOLMatchService(LOLMatchApi matchApi, RIOTApiService riotApiService, LOLChampionNomenclatureRepository championNomenclatureRepository, LOLItemNomenclatureRepository itemNomenclatureRepository, LOLRuneNomenclatureRepository runeNomenclatureRepository, LOLQueueNomenclatureRepository queueNomenclatureRepository, LOLSummonerSpellNomenclatureRepository summonerSpellNomenclatureRepository, LOLMatchRepository matchRepository, RIOTApiKeyRepository apiKeyRepository) {
        this.matchApi = matchApi;
        this.riotApiService = riotApiService;
        this.championNomenclatureRepository = championNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.runeNomenclatureRepository = runeNomenclatureRepository;
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.summonerSpellNomenclatureRepository = summonerSpellNomenclatureRepository;
        this.matchRepository = matchRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<LOLMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, String role) {
        return matchApi.getMatches(puuid, pageNb, queue, role);
    }

    public long getLocalMatchesCount(String puuid, String queue, String role) {
        return matchApi.getMatchCount(puuid, queue, role);
    }

    private void updateMatch(String matchId) { //TODO best serializer
        String apiUrl = LOLMatchHTTPResponse.getUrl(matchId);
        LOLMatchEntity match = riotApiService.callRiotApi(apiUrl, LOLMatchHTTPResponse.class); //TODO use mapper & jackson
        if (this.checkIfQueueIsCompatible(match)) {
            queueEnrichment(match);
            teamsEnrichment(match);
            match.setRemake(match.getTeams().get(0).getParticipants().get(0).isGameEndedInEarlySurrender());
            matchApi.updateMatch(match);
            LOGGER.info("Match {} updated", match.getId());
        } else {
            LOGGER.info("Match {} has incompatible queue", match.getId());
        }
    }

    public void updateMatchTEST(String matchId) throws URISyntaxException, IOException {
        String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s", matchId, "RGAPI-d2db62c3-e922-45d2-bbaf-b29505530f08");
        JsonNode infoNode = new ObjectMapper().readTree(new URI(stringUrl).toURL()).get("info");
        LOLMatchTestEntity match = new ObjectMapper().convertValue(infoNode, LOLMatchTestEntity.class);
        match.setId(matchId);
        match.dispatchParticipantsInTeams();

        LOGGER.info(match.toString());
    }

    /**
     * Due to development API Key rate limit, we recover only and always 100 last matches
     */
    public void updateRemoteToLocalMatches(String puuid) {
        String apiUrl = LOLMatchIdsHTTPResponse.getUrl(puuid, 0, 1); //TODO count pour les tests
        List<String> matchIds = riotApiService.callRiotApiForList(apiUrl, LOLMatchIdsHTTPResponse.class); //TODO use mapper & jackson

        for (String matchId : matchIds) {
            if (this.matchRepository.findFirstById(matchId) != null) {
                break;
            }
            this.updateMatch(matchId);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean checkIfQueueIsCompatible(LOLMatchEntity match) {
        List<String> compatibleQueues = List.of("325", "400", "420", "430", "440", "450", "490", "700", "720", "900", "1020", "1900");
        return compatibleQueues.contains(match.getQueueIdHTTPResponse());
    }


}
