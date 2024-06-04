package com.krazytop.http_response.tft;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.tft.TFTRankEntity;
import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

@Data
public class TFTRankHTTPResponse implements HTTPResponseInterface<TFTRankEntity> {

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

    public static String getUrl(String summonerId) {
        return "https://euw1.api.riotgames.com/tft/league/v1/entries/by-summoner/" + summonerId;
    }

    @Override
    public Class<TFTRankEntity> getEntityClass() {
        return TFTRankEntity.class;
    }
}
