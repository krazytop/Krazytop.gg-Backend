package com.krazytop.service.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(RIOTSummonerService.class);

    private final RIOTSummonerRepository summonerRepository;

    @Autowired
    public RIOTSummonerService(RIOTSummonerRepository summonerRepository) {
        this.summonerRepository = summonerRepository;
    }

    public RIOTSummonerEntity getLocalSummoner(String region, String tag, String name) {
        return this.summonerRepository.findFirstByRegionAndTagAndName(region, "\\b" + tag + "\\b", "\\b" + name + "\\b");
    }

    public RIOTSummonerEntity updateRemoteToLocalSummoner(String region, String tag, String name) {
        RIOTSummonerEntity summoner = getRemoteSummoner(region, tag, name);
        summoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return this.summonerRepository.save(summoner);
    }

    public RIOTSummonerEntity getRemoteSummoner(String region, String tag, String name) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s", name, tag);
            RIOTAccountEntity account = mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
            String summonerApiUrl = String.format("https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/%s", account.getPuuid());
            RIOTSummonerEntity summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummonerEntity.class);

            if (summoner != null) {
                summoner.setRegion(region);
                summoner.setName(account.getName());
                summoner.setTag(account.getTag());
            }
            return summoner;
        } catch (IOException | URISyntaxException e) {
            LOGGER.error("An error occurred while retrieve remote summoner : {}", e.getMessage());
            return null;
        }
    }

}
