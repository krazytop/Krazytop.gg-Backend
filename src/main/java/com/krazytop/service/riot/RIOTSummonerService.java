package com.krazytop.service.riot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.config.CustomHTTPException;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_responses.RIOTHTTPErrorResponsesEnum;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.lol.LOLSummonerRepository;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import com.krazytop.repository.tft.TFTSummonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
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

    public RIOTSummonerEntity getSummoner(String region, String tag, String name, GameEnum game) {
        return getLocalSummoner(region, tag, name, game).orElse(getRemoteSummoner(region, tag, name, game));
    }

    public RIOTSummonerEntity getSummoner(String region, String puuid, GameEnum game) {
        return getLocalSummoner(puuid, game).orElse(getRemoteSummoner(region, puuid, game));
    }

    public RIOTSummonerEntity updateSummoner(String region, String puuid, GameEnum game) {
        // TODO Si la region est mauvaise on check via l'api la nouvelle
        RIOTSummonerEntity summoner = getRemoteSummoner(region, puuid, game);
        summoner.setRegion(region);
        summoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        getRepository(game).save(summoner);
        return summoner;
    }

    private Optional<RIOTSummonerEntity> getLocalSummoner(String puuid, GameEnum game) {
        return getRepository(game).findFirstByPuuid(puuid);
    }

    private Optional<RIOTSummonerEntity> getLocalSummoner(String region, String tag, String name, GameEnum game) {
        return getRepository(game).findFirstByRegionAndTagAndName(region, tag, name);
    }

    private RIOTSummonerEntity getRemoteSummoner(String region, String puuid, GameEnum game) {
        try {
            if (Objects.equals(region, "null")) region = "EUW";
            //TODO check region par api (uniquement utile pour les boards car on stock uniquement les puuid)
            // TODO Si la region est mauvaise on check via l'api la nouvelle
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(game);
            String summonerApiUrl = String.format("https://euw1.api.riotgames.com/%s/summoners/by-puuid/%s?api_key=%s", game.equals(GameEnum.LOL) ? "lol/summoner/v4" : "tft/summoner/v1", puuid, apiKey.getKey());
            RIOTSummonerEntity summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummonerEntity.class);
            RIOTAccountEntity account = getAccount(summoner.getPuuid(), game);
            summoner.setName(account.getName());
            summoner.setTag(account.getTag());
            summoner.setRegion(region);
            return summoner;
        } catch (URISyntaxException | IOException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.SUMMONER_NOT_FOUND);
        }
    }

    private RIOTSummonerEntity getRemoteSummoner(String region, String tag, String name, GameEnum game) {
        try {
            if (!Objects.equals(region, "EUW")) throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.SUMMONER_NOT_FOUND);
            name = name.replace(" ", "%20");
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(game);
            RIOTAccountEntity account = getAccount(tag, name, game);
            String summonerApiUrl = String.format("https://euw1.api.riotgames.com/%s/summoners/by-puuid/%s?api_key=%s", game.equals(GameEnum.LOL) ? "lol/summoner/v4" : "tft/summoner/v1", account.getPuuid(), apiKey.getKey());
            RIOTSummonerEntity summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummonerEntity.class);
            summoner.setRegion(region);
            summoner.setName(account.getName());
            summoner.setTag(account.getTag());
            return summoner;
        } catch (URISyntaxException | IOException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.SUMMONER_NOT_FOUND);
        }
    }

    private RIOTAccountEntity getAccount(String puuid, GameEnum game) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(game);
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/%s?api_key=%s", puuid, apiKey.getKey());
            return mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
        } catch (URISyntaxException | IOException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.ACCOUNT_NOT_FOUND);
        }
    }

    private RIOTAccountEntity getAccount(String tag, String name, GameEnum game) throws URISyntaxException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(game);
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=%s", name, tag, apiKey.getKey());
            return mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
        } catch (URISyntaxException | IOException e) {
            throw new CustomHTTPException(RIOTHTTPErrorResponsesEnum.ACCOUNT_NOT_FOUND);
        }
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
