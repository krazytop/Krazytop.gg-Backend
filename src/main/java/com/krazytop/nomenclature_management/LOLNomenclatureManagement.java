package com.krazytop.nomenclature_management;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
public class LOLNomenclatureManagement { //TODO need better deserialization

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureManagement.class);

    private final LOLQueueNomenclatureRepository queueNomenclatureRepository;
    private final LOLChampionNomenclatureRepository championNomenclatureRepository;
    private final LOLItemNomenclatureRepository itemNomenclatureRepository;
    private final LOLRuneNomenclatureRepository runeNomenclatureRepository;
    private final LOLSummonerSpellNomenclatureRepository summonerSpellNomenclature;
    private final LOLVersionRepository versionRepository;

    @Autowired
    public LOLNomenclatureManagement(LOLQueueNomenclatureRepository queueNomenclatureRepository, LOLChampionNomenclatureRepository championNomenclatureRepository, LOLItemNomenclatureRepository itemNomenclatureRepository, LOLRuneNomenclatureRepository runeNomenclatureRepository, LOLSummonerSpellNomenclatureRepository summonerSpellNomenclature, LOLVersionRepository versionRepository) {
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.championNomenclatureRepository = championNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.runeNomenclatureRepository = runeNomenclatureRepository;
        this.summonerSpellNomenclature = summonerSpellNomenclature;
        this.versionRepository = versionRepository;
    }

    private void updateQueueNomenclature() throws IOException, URISyntaxException {
        queueNomenclatureRepository.deleteAll();
        String uri = "https://static.developer.riotgames.com/docs/lol/queues.json";
        URL url = new URI(uri).toURL();
        ObjectMapper mapper = new ObjectMapper();
        List<LOLQueueNomenclature> queues = mapper.convertValue(mapper.readTree(url), new TypeReference<>() {});
        queueNomenclatureRepository.saveAll(queues);
        LOGGER.info("Update {} queue nomenclatures", queues.size());
    }

    private void updateRuneNomenclature(String version) throws IOException, URISyntaxException {
        runeNomenclatureRepository.deleteAll();
        List<LOLRuneNomenclature> runes = new ArrayList<>();
        JsonNode json = new ObjectMapper().readTree(new URI(String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/runesReforged.json", version)).toURL());

        json.forEach(parentRuneNode -> {
            LOLRuneNomenclature parentRune = new LOLRuneNomenclature();
            parentRune.setName(parentRuneNode.get("name").asText());
            parentRune.setId(parentRuneNode.get("id").asText());
            parentRune.setImage(parentRuneNode.get("icon").asText());
            runes.add(parentRune);
            JsonNode slotsNode = parentRuneNode.path("slots");
            for (JsonNode slotNode : slotsNode) {
                JsonNode runesNode = slotNode.get("runes");
                for (JsonNode runeNode : runesNode) {
                    LOLRuneNomenclature rune = new LOLRuneNomenclature();
                    rune.setName(runeNode.get("name").asText());
                    rune.setId(runeNode.get("id").asText());
                    rune.setImage(runeNode.get("icon").asText());
                    rune.setDescription(runeNode.get("shortDesc").asText());
                    rune.setLongDescription(runeNode.get("longDesc").asText());
                    runes.add(rune);
                }
            }
        });
        runeNomenclatureRepository.saveAll(runes);
        LOGGER.info("Update {} rune nomenclatures", runes.size());
    }

    private void updateChampionNomenclature(String version) throws IOException, URISyntaxException {
        List<LOLChampionNomenclature> champions = new ArrayList<>();
        Map<String, JsonNode> map = this.downloadJsonAndGetMap(String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/champion.json", version));
        for (Map.Entry<String, JsonNode> entry : map.entrySet()) {
            JsonNode value = entry.getValue();
            LOLChampionNomenclature champion = new LOLChampionNomenclature();
            this.setBasicNomenclature(champion, entry);
            champion.setTitle(value.get("title").asText());
            champion.setStats(this.getStringMapFromNode(value.get("stats")));
            champion.setTags(this.getStringListFromNode(value.get("tags")));
            champions.add(champion);
        }
        championNomenclatureRepository.saveAll(champions);
        LOGGER.info("Update {} champion nomenclatures", champions.size());
    }

    private void updateSummonerSpellNomenclature(String version) throws IOException, URISyntaxException {
        List<LOLSummonerSpellNomenclature> summonerSpells = new ArrayList<>();
        Map<String, JsonNode> map = this.downloadJsonAndGetMap(String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/summoner.json", version));
        for (Map.Entry<String, JsonNode> entry : map.entrySet()) {
            JsonNode value = entry.getValue();
            LOLSummonerSpellNomenclature summonerSpell = new LOLSummonerSpellNomenclature();
            this.setBasicNomenclature(summonerSpell, entry);
            summonerSpell.setCooldownBurn(value.get("cooldownBurn").asInt());
            summonerSpells.add(summonerSpell);
        }
        summonerSpellNomenclature.saveAll(summonerSpells);
        LOGGER.info("Update {} summoner spell nomenclatures", summonerSpells.size());
    }

    private void updateItemNomenclature(String version) throws IOException, URISyntaxException {
        List<LOLItemNomenclature> items = new ArrayList<>();
        Map<String, JsonNode> map = this.downloadJsonAndGetMap(String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/item.json", version));
        for (Map.Entry<String, JsonNode> entry : map.entrySet()) {
            JsonNode value = entry.getValue();
            LOLItemNomenclature item = new LOLItemNomenclature();
            this.setBasicNomenclature(item, entry);
            item.setPlainText(value.get("plaintext").asText());
            item.setBaseGold(value.get("gold").get("base").asInt());
            item.setTotalGold(value.get("gold").get("total").asInt());
            item.setTags(this.getStringListFromNode(value.get("tags")));
            item.setStats(this.getStringMapFromNode(value.get("stats")));
            item.setToItems(this.getStringListFromNode(value.get("into")));
            item.setFromItems(this.getStringListFromNode(value.get("from")));
            items.add(item);
        }
        itemNomenclatureRepository.saveAll(items);
        LOGGER.info("Update {} item nomenclatures", items.size());
    }

    private List<String> getStringListFromNode(JsonNode node) {
        return new ObjectMapper().convertValue(node, new TypeReference<>() {});
    }

    private Map<String, Integer> getStringMapFromNode(JsonNode node) {
        return new ObjectMapper().convertValue(node, new TypeReference<>() {});
    }

    private Map<String, JsonNode> downloadJsonAndGetMap(String stringUrl) throws IOException, URISyntaxException {
        JsonNode json = new ObjectMapper().readTree(new URI(stringUrl).toURL()).get("data");
        return new ObjectMapper().convertValue(json, new TypeReference<>() {});
    }

    public void checkNomenclaturesToUpdate() throws IOException, URISyntaxException {
        LOLVersionEntity lastVersion = this.getLastNomenclatureVersions();
        LOLVersionEntity dbVersion = this.versionRepository.findFirstByOrderByItemAsc();
        if (dbVersion == null || !Objects.equals(lastVersion.getItem(), dbVersion.getItem())) {
            this.updateItemNomenclature(lastVersion.getItem());
            this.updateRuneNomenclature(lastVersion.getItem());
            this.updateQueueNomenclature();
        }
        if (dbVersion == null || !Objects.equals(lastVersion.getChampion(), dbVersion.getChampion())) {
            this.updateChampionNomenclature(lastVersion.getChampion());
        }
        if (dbVersion == null || !Objects.equals(lastVersion.getSummoner(), dbVersion.getSummoner())) {
            this.updateSummonerSpellNomenclature(lastVersion.getSummoner());
        }
        this.versionRepository.save(lastVersion);
    }

    private LOLVersionEntity getLastNomenclatureVersions() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(mapper.readTree(url).get("n"), LOLVersionEntity.class);
    }

    private void setBasicNomenclature(LOLNomenclature nomenclature, Map.Entry<String, JsonNode> entry) {
        JsonNode value = entry.getValue();
        nomenclature.setId(value.get("key") != null ? value.get("key").asText() : entry.getKey());
        nomenclature.setName(value.get("name").asText());
        nomenclature.setImage(value.get("image").get("full").asText());
        nomenclature.setDescription(value.get("blurb") != null ? value.get("blurb").asText() : value.get("description").asText());
    }

}
