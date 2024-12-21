package com.krazytop.service.lol;

import com.krazytop.entity.riot.rank.RIOTRankEntity;
import com.krazytop.repository.lol.LOLRankRepository;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class LOLRankService {

    private final LOLRankRepository rankRepository;
    private final RIOTMetadataService metadataService;
    private final RIOTRankService riotRankService;

    @Autowired
    public LOLRankService(LOLRankRepository rankRepository, RIOTMetadataService metadataService, RIOTRankService riotRankService) {
        this.rankRepository = rankRepository;
        this.metadataService = metadataService;
        this.riotRankService = riotRankService;
    }

}
