package com.krazytop.service.riot;

import com.krazytop.api.riot.RIOTSummonerApi;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_response.riot.RIOTAccountHTTPResponse;
import com.krazytop.http_response.riot.RIOTSummonerHTTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RIOTSummonerService {

    private final RIOTSummonerApi riotSummonerApi;
    private final RIOTApiService riotApiService;

    @Autowired
    public RIOTSummonerService(RIOTSummonerApi riotSummonerApi, RIOTApiService riotApiService) {
        this.riotSummonerApi = riotSummonerApi;
        this.riotApiService = riotApiService;
    }

    public RIOTSummonerEntity getLocalSummoner(String region, String tag, String name) {
        return riotSummonerApi.getSummoner(region, tag, name);
    }

    public RIOTSummonerEntity updateRemoteToLocalSummoner(String region, String tag, String name) {
        return riotSummonerApi.updateSummoner(getRemoteSummoner(region, tag, name));
    }

    public RIOTSummonerEntity getRemoteSummoner(String region, String tag, String name) {
        String accountApiUrl = RIOTAccountHTTPResponse.getUrl(tag, name);
        RIOTAccountEntity remoteAccount = riotApiService.callRiotApi(accountApiUrl, RIOTAccountHTTPResponse.class);
        String summonerApiUrl = RIOTSummonerHTTPResponse.getUrl(region, remoteAccount.getPuuid());
        RIOTSummonerEntity remoteSummoner = riotApiService.callRiotApi(summonerApiUrl, RIOTSummonerHTTPResponse.class);
        if (remoteSummoner != null) {
            remoteSummoner.setRegion(region);
            remoteSummoner.setName(remoteAccount.getName());
            remoteSummoner.setTag(remoteAccount.getTag());
        }
        return remoteSummoner;
    }

}
