package com.krazytop.service.lol;

import com.krazytop.api.lol.LOLMatchApi;
import com.krazytop.api.lol.LOLNomenclatureApi;
import com.krazytop.entity.lol.*;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_response.lol.LOLMatchHTTPResponse;
import com.krazytop.http_response.lol.LOLMatchIdsHTTPResponse;
import com.krazytop.http_response.lol.LOLParticipantHTTPResponse;
import com.krazytop.http_response.lol.LOLTeamHTTPResponse;
import com.krazytop.service.riot.RIOTApiService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class LOLMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLMatchService.class);

    private final LOLMatchApi lolMatchApi;
    private final RIOTApiService riotApiService;
    private final LOLNomenclatureApi lolNomenclatureApi;

    @Autowired
    public LOLMatchService(LOLMatchApi lolMatchApi, RIOTApiService riotApiService, LOLNomenclatureApi lolNomenclatureApi) {
        this.lolMatchApi = lolMatchApi;
        this.riotApiService = riotApiService;
        this.lolNomenclatureApi = lolNomenclatureApi;
    }

    public List<LOLMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, String role) {
        return lolMatchApi.getMatches(puuid, pageNb, queue, role);
    }

    public long getLocalMatchesCount(String puuid, String queue, String role) {
        return lolMatchApi.getMatchCount(puuid, queue, role);
    }

    private boolean updateMatch(String matchId) {
        String apiUrl = LOLMatchHTTPResponse.getUrl(matchId);
        LOLMatchEntity match = riotApiService.callRiotApi(apiUrl, LOLMatchHTTPResponse.class);
        queueEnrichment(match);
        teamsEnrichment(match);
        match.setRemake(match.getTeams().get(0).getParticipants().get(0).isGameEndedInEarlySurrender());
        lolMatchApi.updateMatch(match);
        LOGGER.info("Match {} updated", match.getId());
        return true;
    }

    public void updateRemoteToLocalMatches(String puuid) {
        int count = 5; //TODO
        int totalMatchesRecovered = 0;
        int matchesRecovered;
        boolean allMatchesRecovered = false;
        int waitingTime = 1000;
        do {
            String apiUrl = LOLMatchIdsHTTPResponse.getUrl(puuid, totalMatchesRecovered, count);
            List<String> matchIds = riotApiService.callRiotApiForList(apiUrl, LOLMatchIdsHTTPResponse.class);
            totalMatchesRecovered += matchIds.size();
            matchesRecovered = matchIds.size();
            for (String matchId : matchIds) {
                if (!allMatchesRecovered && lolMatchApi.getMatch(matchId) == null) {
                    allMatchesRecovered = !updateMatch(matchId);
                    waitingTime += 2000;
                }
            }
            if (matchesRecovered == count && !allMatchesRecovered) {
                try {
                    Thread.sleep(waitingTime);
                    waitingTime = 1000;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } while (matchesRecovered == 10 && !allMatchesRecovered);
    }

    private void queueEnrichment(LOLMatchEntity match) {
        match.setQueue(lolNomenclatureApi.getQueueNomenclature(match.getQueueIdHTTPResponse()));
        match.setQueueIdHTTPResponse(null);
    }

    private void teamsEnrichment(LOLMatchEntity match) {
        List<LOLTeamEntity> teams = new ArrayList<>();
        for (LOLTeamHTTPResponse teamHTTPResponse : match.getTeamsHTTPResponse()) {
            LOLTeamEntity team = new LOLTeamEntity();
            team.setBannedChampions(teamHTTPResponse.getBans().getBansChampionId().stream()
                    .map(lolNomenclatureApi::getChampionNomenclature)
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
        participant.setItem0(lolNomenclatureApi.getItemNomenclature(participant.getItem0Id()));
        participant.setItem0Id(null);
        participant.setItem1(lolNomenclatureApi.getItemNomenclature(participant.getItem1Id()));
        participant.setItem1Id(null);
        participant.setItem2(lolNomenclatureApi.getItemNomenclature(participant.getItem2Id()));
        participant.setItem2Id(null);
        participant.setItem3(lolNomenclatureApi.getItemNomenclature(participant.getItem3Id()));
        participant.setItem3Id(null);
        participant.setItem4(lolNomenclatureApi.getItemNomenclature(participant.getItem4Id()));
        participant.setItem4Id(null);
        participant.setItem5(lolNomenclatureApi.getItemNomenclature(participant.getItem5Id()));
        participant.setItem5Id(null);
        participant.setWard(lolNomenclatureApi.getItemNomenclature(participant.getWardId()));
        participant.setWardId(null);
    }

    private void championEnrichment(LOLParticipantEntity participant) {
        participant.setChampion(lolNomenclatureApi.getChampionNomenclature(participant.getChampionId()));
        participant.setChampionId(null);
    }

    private void summonerSpellsEnrichment(LOLParticipantEntity participant) {
        participant.setSummonerSpell1(lolNomenclatureApi.getSummonerSpellNomenclature(participant.getSummonerSpellId1()));
        participant.setSummonerSpell2(lolNomenclatureApi.getSummonerSpellNomenclature(participant.getSummonerSpellId2()));
        participant.setSummonerSpellId1(null);
        participant.setSummonerSpellId2(null);
    }

    private void runesEnrichment(LOLParticipantEntity participant) {
        LOLRunesEntity runes = new LOLRunesEntity();
        LOLParticipantHTTPResponse.Perks.RuneStyle primaryRuneStyle = participant.getPerks().getStyles().stream().max(Comparator.comparingInt(style -> style.getSelections().size())).get();
        LOLParticipantHTTPResponse.Perks.RuneStyle secondaryRuneStyle = participant.getPerks().getStyles().stream().min(Comparator.comparingInt(style -> style.getSelections().size())).get();
        runes.setPrimaryRuneCategory(lolNomenclatureApi.getRuneNomenclature(primaryRuneStyle.getStyleId()));
        runes.setPrimaryRuneFirstPerk(lolNomenclatureApi.getRuneNomenclature(primaryRuneStyle.getSelections().get(0).getPerkId()));
        runes.setPrimaryRuneSecondPerk(lolNomenclatureApi.getRuneNomenclature(primaryRuneStyle.getSelections().get(1).getPerkId()));
        runes.setPrimaryRuneThirdPerk(lolNomenclatureApi.getRuneNomenclature(primaryRuneStyle.getSelections().get(2).getPerkId()));
        runes.setSecondaryRuneCategory(lolNomenclatureApi.getRuneNomenclature(secondaryRuneStyle.getStyleId()));
        runes.setSecondaryRuneFirstPerk(lolNomenclatureApi.getRuneNomenclature(secondaryRuneStyle.getSelections().get(0).getPerkId()));
        runes.setSecondaryRuneSecondaryPerk(lolNomenclatureApi.getRuneNomenclature(secondaryRuneStyle.getSelections().get(1).getPerkId()));
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
