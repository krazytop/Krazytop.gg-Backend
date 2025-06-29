package com.krazytop.service.tft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.RIOTSummonerDTO;
import com.krazytop.entity.api_key.ApiKeyEntity;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.entity.riot.RIOTSummoner;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.mapper.tft.TFTSummonerMapper;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.tft.TFTSummonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class TFTSummonerService {

    private final TFTSummonerRepository summonerRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final TFTSummonerMapper summonerMapper;

    @Autowired
    public TFTSummonerService(TFTSummonerRepository summonerRepository, ApiKeyRepository apiKeyRepository, TFTSummonerMapper summonerMapper) {
        this.summonerRepository = summonerRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.summonerMapper = summonerMapper;
    }

    public RIOTSummoner getSummoner(String puuid) {
        return getLocalSummoner(puuid).orElse(getRemoteSummoner(puuid));
    }

    public RIOTSummonerDTO getSummonerDTO(String tag, String name) {
        return summonerMapper.toDTO(getLocalSummoner(tag, name).orElse(getRemoteSummoner(tag, name)));
    }

    public RIOTSummonerDTO getSummonerDTO(String puuid) {
        return summonerMapper.toDTO(getSummoner(puuid));
    }

    private String getRegion(String puuid) {
        return "euw1";//TODO implémenter l'API + appeler ça uniquement si la region est mauvaise pour eco la limit rate ?
    }

    public RIOTSummonerDTO updateSummoner(String puuid) {
        RIOTSummoner summoner = getRemoteSummoner(puuid);
        summoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        summonerRepository.save(summoner);
        return summonerMapper.toDTO(summoner);
    }

    public Optional<RIOTSummoner> getLocalSummoner(String puuid) {
        return summonerRepository.findFirstByPuuid(puuid);
    }

    private Optional<RIOTSummoner> getLocalSummoner(String tag, String name) {
        return summonerRepository.findFirstByTagAndName(tag, name);
    }

    private RIOTSummoner getRemoteSummoner(String puuid) {
        try {
            String region = getRegion(puuid);
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(GameEnum.TFT);
            String summonerApiUrl = String.format("https://%s.api.riotgames.com/tft/summoner/v1/summoners/by-puuid/%s?api_key=%s", region, puuid, apiKey.getKey());
            RIOTSummoner summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummoner.class);
            RIOTAccountEntity account = getAccount(summoner.getPuuid());
            summoner.setName(account.getName());
            summoner.setTag(account.getTag());
            summoner.setRegion(region);
            return summoner;
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.SUMMONER_NOT_FOUND, ex);
        }
    }

    private RIOTSummoner getRemoteSummoner(String tag, String name) {
        try {
            name = name.replace(" ", "%20");
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(GameEnum.TFT);
            RIOTAccountEntity account = getAccount(tag, name);
            String region = getRegion(account.getPuuid());
            String summonerApiUrl = String.format("https://%s.api.riotgames.com/tft/summoner/v1/summoners/by-puuid/%s?api_key=%s", region, account.getPuuid(), apiKey.getKey());
            RIOTSummoner summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), RIOTSummoner.class);
            summoner.setRegion(region);
            summoner.setName(account.getName());
            summoner.setTag(account.getTag());
            return summoner;
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.SUMMONER_NOT_FOUND, ex);
        }
    }

    private RIOTAccountEntity getAccount(String puuid) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(GameEnum.TFT);
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/%s?api_key=%s", puuid, apiKey.getKey());
            return mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.ACCOUNT_NOT_FOUND, ex);
        }
    }

    private RIOTAccountEntity getAccount(String tag, String name) throws URISyntaxException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiKeyEntity apiKey = this.apiKeyRepository.findFirstByGame(GameEnum.TFT);
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=%s", name, tag, apiKey.getKey());
            return mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), RIOTAccountEntity.class);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.ACCOUNT_NOT_FOUND, ex);
        }
    }

    public void updateSpentTimeAndPlayedSeasonsOrSets(String puuid, Long matchDuration, Integer season) {
        Optional<RIOTSummoner> summonerOpt = getLocalSummoner(puuid);
        if (summonerOpt.isPresent()) {
            RIOTSummoner summoner = summonerOpt.get();
            summoner.setSpentTime(summoner.getSpentTime() + matchDuration);
            summoner.getPlayedSeasonsOrSets().add(season);
            summonerRepository.save(summoner);
        }
    }

}
