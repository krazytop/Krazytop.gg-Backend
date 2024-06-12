package com.krazytop.nomenclature_management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRCardRarityNomenclatureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CRNomenclatureManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRNomenclatureManagement.class);

    private static final String FOLDER = "/src/main/resources/data/clash-royal/";

    private final CRAccountLevelNomenclatureRepository accountLevelRepository;
    private final CRCardNomenclatureRepository cardNomenclatureRepository;
    private final CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;

    @Autowired
    public CRNomenclatureManagement(CRAccountLevelNomenclatureRepository accountLevelRepository, CRCardNomenclatureRepository cardNomenclatureRepository, CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository) {
        this.accountLevelRepository = accountLevelRepository;
        this.cardNomenclatureRepository = cardNomenclatureRepository;
        this.cardRarityNomenclatureRepository = cardRarityNomenclatureRepository;
    }

    public boolean updateAccountLevelNomenclature() {
        accountLevelRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/cr-account-levels.json");
            JsonNode dataNode = objectMapper.readTree(itemFile);
            for (JsonNode field : dataNode) {
                CRAccountLevelNomenclature accountLevel = new CRAccountLevelNomenclature();
                accountLevel.setLevel(field.get("name").asInt());
                accountLevel.setTowerLevel(field.get("tower_level").asInt());
                accountLevel.setExpToNextLevel(field.get("exp_to_next_level").asInt());
                accountLevelRepository.save(accountLevel);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating account level nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateCardNomenclature() {
        cardNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/cr-cards.json");
            JsonNode dataNode = objectMapper.readTree(itemFile);
            for (JsonNode field : dataNode) {
                CRCardNomenclature card = new CRCardNomenclature();
                card.setId(field.get("id").asInt());
                card.setName(field.get("name").asText());
                card.setType(field.get("type").asText());
                card.setRarity(field.get("rarity").asText());
                card.setElixir(field.get("elixir").asInt());
                card.setDescription(field.get("description").asText());
                cardNomenclatureRepository.save(card);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating card nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateCardRarityNomenclature() {
        cardRarityNomenclatureRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/cr-cards-rarity.json");
            JsonNode dataNode = objectMapper.readTree(itemFile);
            for (JsonNode field : dataNode) {
                CRCardRarityNomenclature rarity = new CRCardRarityNomenclature();
                rarity.setRelativeLevel(field.get("relative_level").asInt());
                rarity.setName(field.get("name").asText());

                List<Integer> upgradeCostsList = new ArrayList<>();
                JsonNode upgradeCardsCostArray = field.get("upgrade_material_count");
                for (JsonNode costNode : upgradeCardsCostArray) {
                    upgradeCostsList.add(costNode.asInt());
                }
                rarity.setUpgradeCost(upgradeCostsList);
                cardRarityNomenclatureRepository.save(rarity);
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating card nomenclature : {}", e.getMessage());
            return false;
        }
    }

    private String getCurrentWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}
