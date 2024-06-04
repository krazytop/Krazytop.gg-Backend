package com.krazytop.http_response.riot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.krazytop.entity.riot.RIOTAccountEntity;
import com.krazytop.http_response.HTTPResponseInterface;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RIOTAccountHTTPResponse implements HTTPResponseInterface<RIOTAccountEntity> {

    @JsonProperty("tagLine")
    private String tag;
    @JsonProperty("gameName")
    private String name;
    @JsonProperty("puuid")
    private String puuid;

    public static String getUrl(String tag, String name) {
        return "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/" + name + "/" + tag;
    }

    @Override
    public Class<RIOTAccountEntity> getEntityClass() {
        return RIOTAccountEntity.class;
    }

}
