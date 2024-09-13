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
        String stringUrl = String.format("https://europe.api.riotgames.com/lol/match/v5/matches/%s?api_key=%s", matchId, apiKeyRepository.findFirstByOrderByKeyAsc().getKey());
        JsonNode infoNode = new ObjectMapper().readTree(new URI(stringUrl).toURL()).get("info");
        LOLMatchTestEntity match = new LOLMatchTestEntity();
        match.setId(matchId);
        match.setDatetime(infoNode.get("gameCreation").asLong());
        match.setDuration(infoNode.get("gameDuration").asLong());
        match.setVersion(infoNode.get("gameVersion").asText());
        match.setQueue(queueNomenclatureRepository.findFirstById(infoNode.get("queueId").asText()));

        JsonNode teamsNode = infoNode.get("teams");
        List<LOLTeamTestEntity> teams = new ArrayList<>();
        teamsNode.forEach(teamNode -> {
            LOLTeamTestEntity team = new LOLTeamTestEntity();
            team.setHasWin(teamNode.get("win").asBoolean());
            team.setId(teamNode.get("teamId").asText());
            teams.add(team);
        });
        match.setTeams(teams);

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private boolean checkIfQueueIsCompatible(LOLMatchEntity match) {
        List<String> compatibleQueues = List.of("325", "400", "420", "430", "440", "450", "490", "700", "720", "900", "1020", "1900");
        return compatibleQueues.contains(match.getQueueIdHTTPResponse());
    }

    private void queueEnrichment(LOLMatchEntity match) {
        match.setQueue(queueNomenclatureRepository.findFirstById(match.getQueueIdHTTPResponse()));
        match.setQueueIdHTTPResponse(null);
    }

    private void teamsEnrichment(LOLMatchEntity match) {
        List<LOLTeamEntity> teams = new ArrayList<>();
        for (LOLTeamHTTPResponse teamHTTPResponse : match.getTeamsHTTPResponse()) {
            LOLTeamEntity team = new LOLTeamEntity();
            team.setBannedChampions(teamHTTPResponse.getBans().getBansChampionId().stream()
                    .map(championNomenclatureRepository::findFirstById)
                    .toList());
            team.setObjectives(new ModelMapper().map(teamHTTPResponse.getObjectives(), LOLObjectivesEntity.class));
            participantsEnrichment(match, teamHTTPResponse.getId(), team);
            team.setHasWin(teamHTTPResponse.isHasWin());
            teams.add(team);
        }
        match.setTeams(teams);
        match.setParticipantsHTTPResponse(null);
        match.setTeamsHTTPResponse(null);
    }

    private void participantsEnrichment(LOLMatchEntity match, String teamId, LOLTeamEntity team) {
        for (LOLParticipantEntity participant : match.getParticipantsHTTPResponse()) {
            if (Objects.equals(participant.getTeamId(), teamId)) {
                runesEnrichment(participant);
                summonerEnrichment(participant);
                championEnrichment(participant);
                itemsEnrichment(participant);
                summonerSpellsEnrichment(participant);
                team.getParticipants().add(participant);
            }
        }
    }

    private void itemsEnrichment(LOLParticipantEntity participant) {
        participant.setItem0(itemNomenclatureRepository.findFirstById(participant.getItem0Id()));
        participant.setItem0Id(null);
        participant.setItem1(itemNomenclatureRepository.findFirstById(participant.getItem1Id()));
        participant.setItem1Id(null);
        participant.setItem2(itemNomenclatureRepository.findFirstById(participant.getItem2Id()));
        participant.setItem2Id(null);
        participant.setItem3(itemNomenclatureRepository.findFirstById(participant.getItem3Id()));
        participant.setItem3Id(null);
        participant.setItem4(itemNomenclatureRepository.findFirstById(participant.getItem4Id()));
        participant.setItem4Id(null);
        participant.setItem5(itemNomenclatureRepository.findFirstById(participant.getItem5Id()));
        participant.setItem5Id(null);
        participant.setWard(itemNomenclatureRepository.findFirstById(participant.getWardId()));
        participant.setWardId(null);
    }

    private void championEnrichment(LOLParticipantEntity participant) {
        participant.setChampion(championNomenclatureRepository.findFirstById(participant.getChampionId()));
        participant.setChampionId(null);
    }

    private void summonerSpellsEnrichment(LOLParticipantEntity participant) {
        participant.setSummonerSpell1(summonerSpellNomenclatureRepository.findFirstById(participant.getSummonerSpellId1()));
        participant.setSummonerSpell2(summonerSpellNomenclatureRepository.findFirstById(participant.getSummonerSpellId2()));
        participant.setSummonerSpellId1(null);
        participant.setSummonerSpellId2(null);
    }

    private void runesEnrichment(LOLParticipantEntity participant) {
        LOLRunesEntity runes = new LOLRunesEntity();
        LOLParticipantHTTPResponse.Perks.RuneStyle primaryRuneStyle = participant.getPerks().getStyles().stream().max(Comparator.comparingInt(style -> style.getSelections().size())).get();
        LOLParticipantHTTPResponse.Perks.RuneStyle secondaryRuneStyle = participant.getPerks().getStyles().stream().min(Comparator.comparingInt(style -> style.getSelections().size())).get();
        runes.setPrimaryRuneCategory(runeNomenclatureRepository.findFirstById(primaryRuneStyle.getStyleId()));
        runes.setPrimaryRuneFirstPerk(runeNomenclatureRepository.findFirstById(primaryRuneStyle.getSelections().get(0).getPerkId()));
        runes.setPrimaryRuneSecondPerk(runeNomenclatureRepository.findFirstById(primaryRuneStyle.getSelections().get(1).getPerkId()));
        runes.setPrimaryRuneThirdPerk(runeNomenclatureRepository.findFirstById(primaryRuneStyle.getSelections().get(2).getPerkId()));
        runes.setSecondaryRuneCategory(runeNomenclatureRepository.findFirstById(secondaryRuneStyle.getStyleId()));
        runes.setSecondaryRuneFirstPerk(runeNomenclatureRepository.findFirstById(secondaryRuneStyle.getSelections().get(0).getPerkId()));
        runes.setSecondaryRuneSecondaryPerk(runeNomenclatureRepository.findFirstById(secondaryRuneStyle.getSelections().get(1).getPerkId()));
        participant.setRunes(runes);
        participant.setPerks(null);
    }

    private void summonerEnrichment(LOLParticipantEntity participant) {
        RIOTSummonerEntity summoner = new RIOTSummonerEntity();
        summoner.setId(participant.getSummonerId());
        summoner.setName(participant.getRiotIdGameName());
        summoner.setPuuid(participant.getPuuid());
        summoner.setIcon(Integer.parseInt(participant.getProfileIcon()));
        summoner.setLevel(Integer.parseInt(participant.getSummonerLevel()));
        summoner.setTag(participant.getRiotIdTagline());
        participant.setSummoner(summoner);
        participant.setSummonerId(null);
        participant.setRiotIdGameName(null);
        participant.setRiotIdTagline(null);
        participant.setSummonerLevel(null);
        participant.setProfileIcon(null);
        participant.setPuuid(null);
        participant.setTeamId(null);
    }

}
