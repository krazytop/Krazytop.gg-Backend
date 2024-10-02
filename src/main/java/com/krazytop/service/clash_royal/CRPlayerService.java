package com.krazytop.service.clash_royal;

import com.krazytop.entity.clash_royal.*;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardRarityNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRPlayerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CRPlayerService {

    private final CRApiService apiService;
    private final CRPlayerRepository playerRepository;

    @Autowired
    public CRPlayerService(CRApiService apiService, CRPlayerRepository playerRepository) {
        this.apiService = apiService;
        this.playerRepository = playerRepository;
    }

    public CRPlayerEntity getLocalPlayer(String playerId) {
        return playerRepository.findFirstById(playerId);
    }

    public CRPlayerEntity updateRemoteToLocalPlayer(String playerId) throws IOException {
        CRPlayerEntity remotePlayer = getRemotePlayer(playerId);
        chestsEnrichment(remotePlayer);
        remotePlayer.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return playerRepository.save(remotePlayer);
    }

    public CRPlayerEntity getRemotePlayer(String playerId) throws IOException {
        String apiUrl = "https://proxy.royaleapi.dev/v1/players/%23" + playerId;
        CRPlayerEntity player = apiService.callCrApi(apiUrl, CRPlayerEntity.class);
        if (player != null) {//TODO prendre playerId
            player.setId(player.getId().replace("#", ""));
        }
        return player;
    }

    private void chestsEnrichment(CRPlayerEntity player) throws IOException {
        String apiUrl = "https://proxy.royaleapi.dev/v1/players/%23" + player.getId() + "/upcomingchests";
        CRUpcomingChestsEntity chest = apiService.callCrApi(apiUrl, CRUpcomingChestsEntity.class);
        player.setUpcomingChests(chest.getChests());
    }

}
