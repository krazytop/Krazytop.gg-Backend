package com.krazytop.http_response.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RIOTSummonerHTTPResponse implements HTTPResponseInterface<RIOTSummonerEntity> {

    @JsonProperty("id")
    private String id;
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("profileIconId")
    private int iconId;
    @JsonProperty("summonerLevel")
    private int level;
    @JsonProperty("puuid")
    private String puuid;

    public static String getUrl(String region, String puuid) {
        //TODO en fonction de la région adapter l'url
        return "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/" + puuid;
    }

    @Override
    public Class<RIOTSummonerEntity> getEntityClass() {
        return RIOTSummonerEntity.class;
    }

}
