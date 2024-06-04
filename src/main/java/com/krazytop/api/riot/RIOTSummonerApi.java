package com.krazytop.api.riot;

import com.krazytop.entity.riot.RIOTSummonerEntity;
import com.krazytop.repository.riot.RIOTSummonerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class RIOTSummonerApi {

    private final RIOTSummonerRepository riotSummonerRepository;

    @Autowired
    public RIOTSummonerApi(RIOTSummonerRepository riotSummonerRepository) {
        this.riotSummonerRepository = riotSummonerRepository;
    }

    public RIOTSummonerEntity getSummoner(String region, String tag, String name) {
        return riotSummonerRepository.findFirstByRegionAndTagAndName(region, "\\b" + tag + "\\b", "\\b" + name + "\\b");
    }

    public RIOTSummonerEntity updateSummoner(RIOTSummonerEntity summoner) {
        summoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return riotSummonerRepository.save(summoner);
    }
}
