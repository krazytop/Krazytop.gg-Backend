package com.krazytop.nomenclature_management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.repository.lol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Service
public class LOLNomenclatureManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureManagement.class);

    private static final String FOLDER = "/krazytop-core/src/main/resources/data/lol/";

    private final LOLQueueNomenclatureRepository queueNomenclatureRepository;
    private final LOLChampionNomenclatureRepository championNomenclatureRepository;
    private final LOLItemNomenclatureRepository itemNomenclatureRepository;
    private final LOLRuneNomenclatureRepository runeNomenclatureRepository;
    private final LOLSummonerSpellNomenclatureRepository summonerSpellNomenclature;

    @Autowired
    public LOLNomenclatureManagement(LOLQueueNomenclatureRepository queueNomenclatureRepository, LOLChampionNomenclatureRepository championNomenclatureRepository, LOLItemNomenclatureRepository itemNomenclatureRepository, LOLRuneNomenclatureRepository runeNomenclatureRepository, LOLSummonerSpellNomenclatureRepository summonerSpellNomenclature) {
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.championNomenclatureRepository = championNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.runeNomenclatureRepository = runeNomenclatureRepository;
        this.summonerSpellNomenclature = summonerSpellNomenclature;
    }

    public boolean updateQueueNomenclature() {
        queueNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/lol-queues.json");
            JsonNode rootNode = objectMapper.readTree(itemFile);

            for (JsonNode field : rootNode) {
                LOLQueueNomenclature queue = new LOLQueueNomenclature();
                queue.setName(field.get("name").asText());
                queue.setId(field.get("id").asText());
                queueNomenclatureRepository.save(queue);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating queue nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateChampionNomenclature() {
        championNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/lol-champions.json");
            JsonNode rootNode = objectMapper.readTree(itemFile);
            JsonNode dataNode = rootNode.path("data");

            for (JsonNode field : dataNode) {
                LOLChampionNomenclature champion = new LOLChampionNomenclature();
                champion.setName(field.get("name").asText());
                champion.setId(field.get("key").asText());
                champion.setImage(field.path("image").get("full").asText());
                championNomenclatureRepository.save(champion);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating champion nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateItemNomenclature() {
        summonerSpellNomenclature.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/lol-summoner-spells.json");
            JsonNode rootNode = objectMapper.readTree(itemFile);
            JsonNode dataNode = rootNode.path("data");

            for (JsonNode field : dataNode) {
                LOLSummonerSpellNomenclature summonerSpell = new LOLSummonerSpellNomenclature();
                summonerSpell.setName(field.get("name").asText());
                summonerSpell.setId(field.get("key").asText());
                summonerSpell.setImage(field.path("image").get("full").asText());
                summonerSpellNomenclature.save(summonerSpell);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating item nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateSummonerSpellNomenclature() {
        itemNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/lol-items.json");
            JsonNode rootNode = objectMapper.readTree(itemFile);
            JsonNode dataNode = rootNode.path("data");

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = dataNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = fieldsIterator.next();
                String itemId = entry.getKey();
                JsonNode field = entry.getValue();
                LOLItemNomenclature item = new LOLItemNomenclature();
                item.setName(field.get("name").asText());
                item.setId(itemId);
                item.setImage(field.path("image").get("full").asText());
                itemNomenclatureRepository.save(item);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating item nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateRuneNomenclature() {
        runeNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/lol-runes.json");
            JsonNode rootNode = objectMapper.readTree(itemFile);

            for (JsonNode parentRuneNode : rootNode) {
                LOLRuneNomenclature parentRune = new LOLRuneNomenclature();
                parentRune.setName(parentRuneNode.get("name").asText());
                parentRune.setId(parentRuneNode.get("id").asText());
                parentRune.setImage(parentRuneNode.get("icon").asText().split("/")[parentRuneNode.get("icon").asText().split("/").length-1]);
                runeNomenclatureRepository.save(parentRune);
                JsonNode slotsNode = parentRuneNode.path("slots");

                for (JsonNode slotNode : slotsNode) {
                    JsonNode runesNode = slotNode.get("runes");
                    for (JsonNode runeNode : runesNode) {
                        LOLRuneNomenclature rune = new LOLRuneNomenclature();
                        rune.setName(runeNode.get("name").asText());
                        rune.setId(runeNode.get("id").asText());
                        rune.setImage(runeNode.get("icon").asText().split("/")[runeNode.get("icon").asText().split("/").length-1]);
                        runeNomenclatureRepository.save(rune);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating rune nomenclature : {}", e.getMessage());
            return false;
        }
    }

    private String getCurrentWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}
