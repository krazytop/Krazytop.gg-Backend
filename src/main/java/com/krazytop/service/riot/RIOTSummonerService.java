package com.krazytop.service.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class RIOTSummonerService {

    private final RIOTSummonerRepository summonerRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public RIOTSummonerService(RIOTSummonerRepository summonerRepository, ApiKeyRepository apiKeyRepository) {
        this.summonerRepository = summonerRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public RIOTSummonerEntity getLocalSummoner(String region, String tag, String name) {
        return this.summonerRepository.findFirstByRegionAndTagAndName(region, "\\b" + tag + "\\b", "\\b" + name + "\\b");
    }

    public void updateRemoteToLocalSummoner(String region, String tag, String name) throws URISyntaxException, IOException {
        RIOTSummonerEntity summoner = getRemoteSummoner(region, tag, name);
        summoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        summonerRepository.save(summoner);
    }

    public RIOTSummonerEntity getRemoteSummoner(String region, String tag, String name) throws URISyntaxException, IOException {
        name = name.replace(" ", "%20");
        ObjectMapper mapper = new ObjectMapper();
        ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(GameEnum.RIOT);
        String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=%s", name, tag, apiKey.getKey());
        RIOTAccountEntity account = mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
        String summonerApiUrl = String.format("https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/%s?api_key=%s", account.getPuuid(), apiKey.getKey());
        RIOTSummonerEntity summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummonerEntity.class);
        summoner.setRegion(region);
        summoner.setName(account.getName());
        summoner.setTag(account.getTag());
        return summoner;
    }

    public void updateTimeSpentOnLOL(String puuid, Long matchDuration) {
        RIOTSummonerEntity summoner = summonerRepository.findFirstByPuuid(puuid);
        if (summoner.getSpentTimeOnLOL() == null) {
            summoner.setSpentTimeOnLOL(matchDuration);
        } else {
            summoner.setSpentTimeOnLOL(summoner.getSpentTimeOnLOL() + matchDuration);
        }
        summonerRepository.save(summoner);
    }

    public void updateTimeSpentOnTFT(String puuid, Long matchDuration) {
        RIOTSummonerEntity summoner = summonerRepository.findFirstByPuuid(puuid);
        if (summoner.getSpentTimeOnTFT() == null) {
            summoner.setSpentTimeOnTFT(matchDuration);
        } else {
            summoner.setSpentTimeOnTFT(summoner.getSpentTimeOnTFT() + matchDuration);
        }
        summonerRepository.save(summoner);
    }

}
