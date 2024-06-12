package com.krazytop.nomenclature_management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.tft.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class TFTNomenclatureManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTNomenclatureManagement.class);

    private static final List<String> VERSIONS = List.of("set8", "set8_5", "set9", "set9_5");
    private static final String FOLDER = "/src/main/resources/data/tft/";

    private final TFTTraitNomenclatureRepository traitNomenclatureRepository;
    private final TFTUnitNomenclatureRepository unitNomenclatureRepository;
    private final TFTQueueNomenclatureRepository queueNomenclatureRepository;
    private final TFTItemNomenclatureRepository itemNomenclatureRepository;
    private final TFTAugmentNomenclatureRepository augmentNomenclatureRepository;

    @Autowired
    public TFTNomenclatureManagement(TFTTraitNomenclatureRepository traitNomenclatureRepository, TFTUnitNomenclatureRepository unitNomenclatureRepository, TFTQueueNomenclatureRepository queueNomenclatureRepository, TFTItemNomenclatureRepository itemNomenclatureRepository, TFTAugmentNomenclatureRepository augmentNomenclatureRepository) {
        this.traitNomenclatureRepository = traitNomenclatureRepository;
        this.unitNomenclatureRepository = unitNomenclatureRepository;
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.augmentNomenclatureRepository = augmentNomenclatureRepository;
    }

    public boolean updateTraitNomenclature() {
        traitNomenclatureRepository.deleteAll();
        try {
            for (String version : VERSIONS) {
                ObjectMapper objectMapper = new ObjectMapper();
                File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + version + "/tft-traits.json");
                JsonNode rootNode = objectMapper.readTree(itemFile);
                JsonNode dataNode = rootNode.path("data");

                for (JsonNode field : dataNode) {
                    TFTTraitNomenclature trait = new TFTTraitNomenclature();
                    trait.setName(field.get("name").asText());
                    trait.setId(field.get("id").asText());
                    trait.setImage(field.path("image").get("full").asText());
                    traitNomenclatureRepository.save(trait);
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating trait nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateUnitNomenclature() {
        unitNomenclatureRepository.deleteAll();
        try {
            for (String version : VERSIONS) {
                ObjectMapper objectMapper = new ObjectMapper();
                File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + version + "/tft-units.json");
                JsonNode rootNode = objectMapper.readTree(itemFile);
                JsonNode dataNode = rootNode.path("data");

                for (JsonNode field : dataNode) {
                    TFTUnitNomenclature unit = new TFTUnitNomenclature();
                    unit.setName(field.get("name").asText());
                    unit.setId(field.get("id").asText());
                    unitNomenclatureRepository.save(unit);
                }
                // Add other specials units
                TFTUnitNomenclature tHex = new TFTUnitNomenclature();
                tHex.setId("TFT9_THex");
                tHex.setName("T-Hex");
                unitNomenclatureRepository.save(tHex);
                TFTUnitNomenclature turret = new TFTUnitNomenclature();
                turret.setId("TFT9_HeimerdingerTurret");
                turret.setName("Heimerdinger-Turret");
                unitNomenclatureRepository.save(turret);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating unit nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateQueueNomenclature() {
        queueNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/tft-queues.json");
            JsonNode rootNode = objectMapper.readTree(itemFile);
            JsonNode dataNode = rootNode.path("data");

            for (JsonNode field : dataNode) {
                TFTQueueNomenclature queue = new TFTQueueNomenclature();
                queue.setName(field.get("name").asText());
                queue.setQueueType(field.get("queueType").asText());
                queue.setId(field.get("id").asText());
                queue.setImage(field.path("image").get("full").asText());
                queueNomenclatureRepository.save(queue);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating queue nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateItemNomenclature() {
        itemNomenclatureRepository.deleteAll();
        try {
            for (String version : VERSIONS) {
                ObjectMapper objectMapper = new ObjectMapper();
                File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + version + "/tft-items.json");
                JsonNode rootNode = objectMapper.readTree(itemFile);
                JsonNode dataNode = rootNode.path("data");

                for (JsonNode field : dataNode) {
                    TFTItemNomenclature item = new TFTItemNomenclature();
                    item.setName(field.get("name").asText());
                    item.setId(field.get("id").asText());
                    item.setImage(field.path("image").get("full").asText());
                    itemNomenclatureRepository.save(item);
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating item nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateAugmentNomenclature() {
        augmentNomenclatureRepository.deleteAll();
        try {
            for (String version : VERSIONS) {
                ObjectMapper objectMapper = new ObjectMapper();
                File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + version + "/tft-augments.json");
                JsonNode rootNode = objectMapper.readTree(itemFile);
                JsonNode dataNode = rootNode.path("data");

                for (JsonNode field : dataNode) {
                    TFTAugmentNomenclature augment = new TFTAugmentNomenclature();
                    augment.setName(field.get("name").asText());
                    augment.setId(field.get("id").asText());
                    augment.setImage(field.path("image").get("full").asText());
                    augmentNomenclatureRepository.save(augment);
                }
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating augment nomenclature : {}", e.getMessage());
            return false;
        }
    }

    private String getCurrentWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}
