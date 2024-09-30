package com.krazytop.service.clash_royal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.clash_royal.CRApiKeyEntity;
import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRApiKeyRepository;
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
public class CRNomenclatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRNomenclatureService.class);

    private static final String FOLDER = "/src/main/resources/data/clash-royal/";

    private final CRAccountLevelNomenclatureRepository accountLevelRepository;
    private final CRCardNomenclatureRepository cardNomenclatureRepository;
    private final CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;
    private final CRApiKeyRepository apiKeyRepository;

    @Autowired
    public CRNomenclatureService(CRAccountLevelNomenclatureRepository accountLevelRepository, CRCardNomenclatureRepository cardNomenclatureRepository, CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository, CRApiKeyRepository apiKeyRepository) {
        this.accountLevelRepository = accountLevelRepository;
        this.cardNomenclatureRepository = cardNomenclatureRepository;
        this.cardRarityNomenclatureRepository = cardRarityNomenclatureRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    public boolean updateAccountLevelNomenclature() {
        accountLevelRepository.deleteAll();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "/cr-account-levels.json");
            JsonNode dataNode = objectMapper.readTree(itemFile);
            List<CRAccountLevelNomenclature> accountLevels = new ArrayList<>();
            for (JsonNode field : dataNode) {
                CRAccountLevelNomenclature accountLevel = new CRAccountLevelNomenclature();
                accountLevel.setLevel(field.get("name").asInt());
                accountLevel.setTowerLevel(field.get("tower_level").asInt());
                accountLevel.setExpToNextLevel(field.get("exp_to_next_level").asInt());
                accountLevels.add(accountLevel);
            }
            accountLevelRepository.saveAll(accountLevels);
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
            List<CRCardNomenclature> cards = new ArrayList<>();
            for (JsonNode field : dataNode) {
                CRCardNomenclature card = new CRCardNomenclature();
                card.setId(field.get("id").asInt());
                card.setName(field.get("name").asText());
                card.setType(field.get("type").asText());
                card.setRarity(field.get("rarity").asText());
                card.setElixir(field.get("elixir").asInt());
                card.setDescription(field.get("description").asText());
                cards.add(card);
            }
            cardNomenclatureRepository.saveAll(cards);
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
            List<CRCardRarityNomenclature> rarities = new ArrayList<>();
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
                rarities.add(rarity);
            }
            cardRarityNomenclatureRepository.saveAll(rarities);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error while updating card nomenclature : {}", e.getMessage());
            return false;
        }
    }

    public boolean updateApiKey(String apiKey) {
        apiKeyRepository.deleteAll();
        apiKeyRepository.save(new CRApiKeyEntity(apiKey));
        return true;
    }

    private String getCurrentWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}
