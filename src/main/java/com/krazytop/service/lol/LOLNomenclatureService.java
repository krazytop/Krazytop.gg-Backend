package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.lol.LOLVersionEntity;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.lol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * Only EUW & fr_FR for now
 */
@Service
public class LOLNomenclatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureService.class);

    private final LOLQueueNomenclatureRepository queueNomenclatureRepository;
    private final LOLChampionNomenclatureRepository championNomenclatureRepository;
    private final LOLItemNomenclatureRepository itemNomenclatureRepository;
    private final LOLRuneNomenclatureRepository runeNomenclatureRepository;
    private final LOLSummonerSpellNomenclatureRepository summonerSpellNomenclature;
    private final LOLVersionRepository versionRepository;

    @Autowired
    public LOLNomenclatureService(LOLQueueNomenclatureRepository queueNomenclatureRepository, LOLChampionNomenclatureRepository championNomenclatureRepository, LOLItemNomenclatureRepository itemNomenclatureRepository, LOLRuneNomenclatureRepository runeNomenclatureRepository, LOLSummonerSpellNomenclatureRepository summonerSpellNomenclature, LOLVersionRepository versionRepository) {
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.championNomenclatureRepository = championNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.runeNomenclatureRepository = runeNomenclatureRepository;
        this.summonerSpellNomenclature = summonerSpellNomenclature;
        this.versionRepository = versionRepository;
    }

    private void updateQueueNomenclature() throws IOException, URISyntaxException {
        String uri = "https://static.developer.riotgames.com/docs/lol/queues.json";
        URL url = new URI(uri).toURL();
        ObjectMapper mapper = new ObjectMapper();
        List<LOLQueueNomenclature> queues = mapper.convertValue(mapper.readTree(url), new TypeReference<>() {});
        queueNomenclatureRepository.saveAll(queues);
        LOGGER.info("Update {} LOL queue nomenclatures", queues.size());
    }

    private void updateChampionNomenclature(String version) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/champion.json", version);
        Map<String, LOLChampionNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        List<LOLChampionNomenclature> nomenclatures = nomenclaturesMap.values().stream().toList();
        championNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} LOL champion nomenclatures", nomenclatures.size());
    }

    private void updateSummonerSpellNomenclature(String version) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/summoner.json", version);
        Map<String, LOLSummonerSpellNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
        List<LOLSummonerSpellNomenclature> nomenclatures = nomenclaturesMap.values().stream().toList();
        summonerSpellNomenclature.saveAll(nomenclatures);
        LOGGER.info("Update {} LOL summoner spell nomenclatures", nomenclatures.size());
    }

    private void updateItemNomenclature(String version) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/item.json", version);
        Map<String, LOLItemNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
        List<LOLItemNomenclature> nomenclatures = nomenclaturesMap.values().stream().toList();
        itemNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} LOL item nomenclatures", nomenclatures.size());
    }

    private void updateRuneNomenclature(String version) throws IOException, URISyntaxException {
        List<LOLRuneNomenclature> runes = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.readTree(new URI(String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/runesReforged.json", version)).toURL())
                .forEach(parentRuneNode -> {
                    runes.add(mapper.convertValue(parentRuneNode, new TypeReference<>() {}));
                    parentRuneNode.path("slots").forEach(slotNode -> slotNode.get("runes").forEach(runeNode -> runes.add(mapper.convertValue(runeNode, new TypeReference<>() {}))));
                });
        runeNomenclatureRepository.saveAll(runes);
        LOGGER.info("Update {} LOL rune nomenclatures", runes.size());
    }

    public boolean updateAllNomenclatures() throws IOException, URISyntaxException {
        LOLVersionEntity lastVersion = this.getLastNomenclatureVersions();
        LOLVersionEntity dbVersion = this.versionRepository.findFirstByOrderByItemAsc();
        boolean nomenclaturesUpdated = false;
        if (dbVersion == null || !Objects.equals(lastVersion.getItem(), dbVersion.getItem())) {
            this.updateItemNomenclature(lastVersion.getItem());
            this.updateRuneNomenclature(lastVersion.getItem());
            this.updateQueueNomenclature();
            nomenclaturesUpdated = true;
        }
        if (dbVersion == null || !Objects.equals(lastVersion.getChampion(), dbVersion.getChampion())) {
            this.updateChampionNomenclature(lastVersion.getChampion());
            nomenclaturesUpdated = true;
        }
        if (dbVersion == null || !Objects.equals(lastVersion.getSummoner(), dbVersion.getSummoner())) {
            this.updateSummonerSpellNomenclature(lastVersion.getSummoner());
            nomenclaturesUpdated = true;
        }
        if (nomenclaturesUpdated) {
            this.versionRepository.save(lastVersion);
        }
        return nomenclaturesUpdated;
    }

    private LOLVersionEntity getLastNomenclatureVersions() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(mapper.readTree(url).get("n"), LOLVersionEntity.class);
    }

}