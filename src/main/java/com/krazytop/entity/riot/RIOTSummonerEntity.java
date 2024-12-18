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
    private Integer level;
    @JsonAlias("profileIconId")
    private Integer icon;
    private String puuid;
    private String tag;
    private String region;
    private Date updateDate;
    private Long spentTimeOnLOL;
    private Long spentTimeOnTFT;

}
