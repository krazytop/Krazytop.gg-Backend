package com.krazytop.http_response.lol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.lol.LOLRankEntity;
import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

@Data
public class LOLRankHTTPResponse implements HTTPResponseInterface<LOLRankEntity> {

    @JsonProperty("summonerId")
    private String summonerId;

    @JsonProperty("tier")
    private String tier;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("queueType")
    private String queueType;

    @JsonProperty("leaguePoints")
    private int leaguePoints;

    @JsonProperty("wins")
    private int wins;

    @JsonProperty("losses")
    private int losses;


    public static String getUrl(String summonerId) {
        return "https://euw1.api.riotgames.com/lol/league/v4/entries/by-summoner/" + summonerId;
    }

    @Override
    public Class<LOLRankEntity> getEntityClass() {
        return LOLRankEntity.class;
    }
}
