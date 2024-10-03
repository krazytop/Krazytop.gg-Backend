package com.krazytop.service.clash_royal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.clash_royal.*;
import com.krazytop.nomenclature.GameEnum;
import com.krazytop.repository.api_key.ApiKeyRepository;
import com.krazytop.repository.clash_royal.CRPlayerRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CRPlayerService {

    private final CRPlayerRepository playerRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public CRPlayerService(ApiKeyRepository apiKeyRepository, CRPlayerRepository playerRepository) {
        this.apiKeyRepository = apiKeyRepository;
        this.playerRepository = playerRepository;
    }

    public CRPlayerEntity getLocalPlayer(String playerId) {
        return playerRepository.findFirstById(playerId);
    }

    public void updateRemoteToLocalPlayer(String playerId) throws IOException {
        CRPlayerEntity remotePlayer = getRemotePlayer(playerId);
        //chestsEnrichment(remotePlayer);
        remotePlayer.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        playerRepository.save(remotePlayer);
    }

    public CRPlayerEntity getRemotePlayer(String playerId) throws IOException {
        String apiUrl = "https://proxy.royaleapi.dev/v1/players/%23" + playerId;
        CRPlayerEntity player = callCrApi(apiUrl, CRPlayerEntity.class);
        player.setId(playerId);
        return player;
    }

    private void chestsEnrichment(CRPlayerEntity player) throws IOException {
        String apiUrl = "https://proxy.royaleapi.dev/v1/players/%23" + player.getId() + "/upcomingchests";
        CRUpcomingChestsEntity chest = callCrApi(apiUrl, CRUpcomingChestsEntity.class);
        player.setUpcomingChests(chest.getChests());
    }

    public <T> T callCrApi(String apiUrl, Class<T> responseTypeClass) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(apiUrl);
            httpGet.addHeader("Authorization", "Bearer " + apiKeyRepository.findFirstByGame(GameEnum.CLASH_ROYAL).getKey());
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                return new ObjectMapper().readValue(response.getEntity().getContent(), responseTypeClass);
            }
        }
    }

}
