package com.krazytop.entity.riot;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "Summoner")
public class RIOTSummonerEntity {

    private String id;
    private String tag;
    private String name;
    private String accountId;
    private int level;
    private int icon;
    private String puuid;
    private String region;
    private Date updateDate;

}
