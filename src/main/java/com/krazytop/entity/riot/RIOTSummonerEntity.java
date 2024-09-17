package com.krazytop.entity.riot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Summoner")
public class RIOTSummonerEntity {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("summonerLevel")
    private int level;
    @JsonProperty("profileIconId")
    private int icon;
    @JsonProperty("puuid")
    private String puuid;
    private String tag;
    private String region;
    private Date updateDate;

    public RIOTSummonerEntity(String id, String puuid, String name, String tag, int level, int icon) {
        this.id = id;
        this.puuid = puuid;
        this.name = name;
        this.tag = tag;
        this.level = level;
        this.icon = icon;
    }
}
