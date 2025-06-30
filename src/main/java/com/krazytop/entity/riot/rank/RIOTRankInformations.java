package com.krazytop.entity.riot.rank;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RIOTRankInformations {

    private Date date;
    @JsonAlias({"tier", "ratedTier"})
    private String tier;
    private String rank;
    @JsonAlias({"leaguePoints", "ratedRating"})
    private Integer leaguePoints;
    private Integer wins;
    private Integer losses;
}