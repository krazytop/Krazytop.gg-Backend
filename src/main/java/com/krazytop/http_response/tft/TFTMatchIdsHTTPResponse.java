package com.krazytop.http_response.tft;

import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

import java.util.List;

@Data
public class TFTMatchIdsHTTPResponse implements HTTPResponseInterface<String> {

    private List<String> matchIds;

    public static String getUrl(String puuid, int start, int count) {
        return "https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/" + puuid + "/ids" + "?start=" + start + "&count=" + count;
    }

    @Override
    public Class<String> getEntityClass() {
        return String.class;
    }

}
