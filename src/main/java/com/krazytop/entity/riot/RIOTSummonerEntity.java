package com.krazytop.entity.riot;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "Summoner")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RIOTSummonerEntity {

    private String id;
    @JsonAlias("gameName")
    private String name;
    private String accountId;
    @JsonAlias("summonerLevel")
    private Integer level;
    @JsonAlias("profileIconId")
    private Integer icon;
    private String puuid;
    @JsonAlias("tagLine")
    private String tag;
    private String region;
    private Date updateDate;
    private Long spentTime = 0L;
    private Set<Integer> playedSeasonsOrSets = new HashSet<>();

}
