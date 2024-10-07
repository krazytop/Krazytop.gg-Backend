package com.krazytop.entity.riot;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Summoner")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RIOTSummonerEntity {

    private String id;
    private String name;
    private String accountId;
    @JsonAlias("summonerLevel")
    private int level;
    @JsonAlias("profileIconId")
    private int icon;
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
