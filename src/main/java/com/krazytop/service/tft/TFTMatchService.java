package com.krazytop.service.tft;

import com.krazytop.api.tft.TFTMatchApi;
import com.krazytop.api.tft.TFTNomenclatureApi;
import com.krazytop.entity.tft.TFTMatchEntity;
import com.krazytop.entity.tft.TFTParticipantEntity;
import com.krazytop.entity.tft.TFTTraitEntity;
import com.krazytop.entity.tft.TFTUnitEntity;
import com.krazytop.http_response.tft.TFTMatchHTTPResponse;
import com.krazytop.http_response.tft.TFTMatchIdsHTTPResponse;
import com.krazytop.nomenclature.tft.TFTAugmentNomenclature;
import com.krazytop.nomenclature.tft.TFTItemNomenclature;
import com.krazytop.nomenclature.tft.TFTTraitNomenclature;
import com.krazytop.nomenclature.tft.TFTUnitNomenclature;
import com.krazytop.service.riot.RIOTApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TFTMatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTMatchService.class);

    private final TFTMatchApi tftMatchApi;
    private final TFTNomenclatureApi tftNomenclatureApi;
    private final RIOTApiService riotApiService;

    @Autowired
    public TFTMatchService(TFTMatchApi tftMatchApi, TFTNomenclatureApi tftNomenclatureApi, RIOTApiService riotApiService) {
        this.tftMatchApi = tftMatchApi;
        this.tftNomenclatureApi = tftNomenclatureApi;
        this.riotApiService = riotApiService;
    }

    public List<TFTMatchEntity> getLocalMatches(String puuid, int pageNb, String queue, String set) {
        return tftMatchApi.getMatches(puuid, pageNb, queue, set);
    }

    public long getLocalMatchesCount(String puuid, String queue, String set) {
        return tftMatchApi.getMatchCount(puuid, queue, set);
    }

    private boolean updateMatch(String matchId) {
        String apiUrl = TFTMatchHTTPResponse.getUrl(matchId);
        TFTMatchEntity match = riotApiService.callRiotApi(apiUrl, TFTMatchHTTPResponse.class);
        List<String> versions = List.of("TFTSet9", "TFTSet9_2", "TFTSet8", "TFTSet8_2");
        if (!versions.contains(match.getSet())) {
            LOGGER.info("Match {} is not in supported sets", match.getId());
            return false;
        } else {
            queueEnrichment(match);
            for (TFTParticipantEntity participant : match.getParticipants()) {
                augmentsEnrichment(participant);
                traitsEnrichment(participant);
                unitsEnrichment(participant);/*
                if (Objects.equals(match.getQueue().getQueueType(), "RANKED_TFT_DOUBLE_UP")) {
                    int originalPlacement = participant.getPlacement();
                    int adjustedPlacement = (int) Math.ceil((double) originalPlacement / 2);
                    participant.setPlacement(adjustedPlacement);
                }*/
            }
            modifySet(match);
            tftMatchApi.updateMatch(match);
            LOGGER.info("Match {} updated", match.getId());
            return true;
        }
    }
/*
    public void getNewMatches(String puuid) {
        int count = 1;//TODO changer à 10
        ExecutorService executor = Executors.newFixedThreadPool(2);
        AtomicInteger matchesRecovered = new AtomicInteger();
        executor.submit(() -> {
            do {
                String apiUrl = TFTMatchIdsHTTPResponse.getUrl(puuid, tftMatchApi.getMatchCount(), count);
                List<String> matchIds = riotApiService.callRiotApi(apiUrl, List.class);
                matchesRecovered.set(matchIds.size());
                for (String matchId : matchIds) {
                    updateMatch(matchId);
                }
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } while (matchesRecovered.get() == 11);//TODO changer à 10
        });
    }
*/

    public void updateRemoteToLocalMatches(String puuid) {
        int count = 10;
        int totalMatchesRecovered = 0;
        int matchesRecovered;
        boolean allMatchesRecovered = false;
        int waitingTime = 1000;
        do {
            String apiUrl = TFTMatchIdsHTTPResponse.getUrl(puuid, totalMatchesRecovered, count);
            List<String> matchIds = riotApiService.callRiotApiForList(apiUrl, TFTMatchIdsHTTPResponse.class);
            totalMatchesRecovered += matchIds.size();
            matchesRecovered = matchIds.size();
            for (String matchId : matchIds) {
                if (!allMatchesRecovered && tftMatchApi.getMatch(matchId) == null) {
                    allMatchesRecovered = !updateMatch(matchId);
                    waitingTime += 2000;
                }
            }
            if (matchesRecovered == count && !allMatchesRecovered) {
                try {
                    Thread.sleep(waitingTime);
                    waitingTime = 1000;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } while (matchesRecovered == 10 && !allMatchesRecovered);
    }

    private void augmentsEnrichment(TFTParticipantEntity participant) {
        List<TFTAugmentNomenclature> augments = new ArrayList<>();
        for (String augmentId : participant.getAugmentsIds()) {
            TFTAugmentNomenclature augment = tftNomenclatureApi.getAugmentNomenclature(augmentId);
            augments.add(augment);
        }
        participant.setAugments(augments);
        participant.setAugmentsIds(null);
    }

    private void itemsEnrichment(TFTUnitEntity unit) {
        List<TFTItemNomenclature> items = new ArrayList<>();
        for (String itemId : unit.getItemsIds()) {
            TFTItemNomenclature item = tftNomenclatureApi.getItemNomenclature(itemId);
            items.add(item);
        }
        unit.setItems(items);
        unit.setItemsIds(null);
    }

    private void traitsEnrichment(TFTParticipantEntity participant) {
        for (TFTTraitEntity trait : participant.getTraits()) {
            TFTTraitNomenclature traitNomenclature = tftNomenclatureApi.getTraitNomenclature(trait.getId());
            trait.setName(traitNomenclature.getName());
            trait.setImage(traitNomenclature.getImage());
        }
    }

    private void unitsEnrichment(TFTParticipantEntity participant) {
        for (TFTUnitEntity unit : participant.getUnits()) {
            TFTUnitNomenclature unitNomenclature = tftNomenclatureApi.getUnitNomenclature(unit.getId());
            unit.setName(unitNomenclature.getName());
            itemsEnrichment(unit);
        }
    }

    private void queueEnrichment(TFTMatchEntity match) {
        match.setQueue(tftNomenclatureApi.getQueueNomenclature(match.getQueueId()));
        match.setQueueId(null);
    }

    private void modifySet(TFTMatchEntity match) {
        Map<String, String> setMappings = new HashMap<>();
        setMappings.put("TFTSet9_2", "set_9-5");
        setMappings.put("TFTSet9", "set_9");
        setMappings.put("TFTSet8_2", "set_8-5");
        setMappings.put("TFTSet8", "set_8");

        match.setSet(setMappings.get(match.getSet()));
    }



}
