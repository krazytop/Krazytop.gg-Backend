package com.krazytop.service.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLSummonerRepository;
import com.krazytop.repository.riot.RIOTRankRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import com.krazytop.repository.tft.TFTSummonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@Service
public class RIOTSummonerService {

    private final LOLSummonerRepository lolSummonerRepository;
    private final TFTSummonerRepository tftSummonerRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public RIOTSummonerService(LOLSummonerRepository lolSummonerRepository, TFTSummonerRepository tftSummonerRepository, ApiKeyRepository apiKeyRepository) {
        this.lolSummonerRepository = lolSummonerRepository;
        this.tftSummonerRepository = tftSummonerRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public Optional<RIOTSummonerEntity> getLocalSummoner(String puuid, GameEnum game) {
        return getRepository(game).findFirstByPuuid(puuid);
    }

    public Optional<RIOTSummonerEntity> getLocalSummoner(String region, String tag, String name, GameEnum game) {
        return getRepository(game)
                .findFirstByRegionAndTagAndName(region, tag, name);
    }

    public void updateRemoteToLocalSummoner(String region, String tag, String name, GameEnum game) throws URISyntaxException, IOException {
        RIOTSummonerEntity summoner = getRemoteSummonerByNameAndTag(region, tag, name, game);
        summoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        getRepository(game).save(summoner);
    }

    public RIOTSummonerEntity getRemoteSummonerByNameAndTag(String region, String tag, String name, GameEnum game) throws URISyntaxException, IOException {
        name = name.replace(" ", "%20");
        ObjectMapper mapper = new ObjectMapper();
        ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(game);
        String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=%s", name, tag, apiKey.getKey());
        RIOTAccountEntity account = mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
        String summonerApiUrl = String.format("https://euw1.api.riotgames.com/%s/summoners/by-puuid/%s?api_key=%s", game.equals(GameEnum.LOL) ? "lol/summoner/v4" : "tft/summoner/v1", account.getPuuid(), apiKey.getKey());
        RIOTSummonerEntity summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummonerEntity.class);
        summoner.setRegion(region);
        summoner.setName(account.getName());
        summoner.setTag(account.getTag());
        return summoner;
    }

    public RIOTSummonerEntity getRemoteSummonerByPuuid(String puuid) throws URISyntaxException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(GameEnum.TFT);
        String summonerApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/%s?api_key=%s", puuid, apiKey.getKey());
        return mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummonerEntity.class);
    }

    public void updateSpentTimeAndPlayedSeasonsOrSets(String puuid, Long matchDuration, Integer season, GameEnum game) {
        Optional<RIOTSummonerEntity> summonerOpt = getLocalSummoner(puuid, game);
        if (summonerOpt.isPresent()) {
            RIOTSummonerEntity summoner = summonerOpt.get();
            summoner.setSpentTime(summoner.getSpentTime() + matchDuration);
            summoner.getPlayedSeasonsOrSets().add(season);
            getRepository(game).save(summoner);
        }
    }

    private RIOTSummonerRepository getRepository(GameEnum game) {
        return game.equals(GameEnum.LOL) ? lolSummonerRepository : tftSummonerRepository;
    }

}
