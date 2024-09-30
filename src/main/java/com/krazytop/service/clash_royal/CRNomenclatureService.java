package com.krazytop.service.clash_royal;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class CRNomenclatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CRNomenclatureService.class);

    private final CRAccountLevelNomenclatureRepository accountLevelRepository;
    private final CRCardNomenclatureRepository cardNomenclatureRepository;
    private final CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository;

    @Autowired
    public CRNomenclatureService(CRAccountLevelNomenclatureRepository accountLevelRepository, CRCardNomenclatureRepository cardNomenclatureRepository, CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository) {
        this.accountLevelRepository = accountLevelRepository;
        this.cardNomenclatureRepository = cardNomenclatureRepository;
        this.cardRarityNomenclatureRepository = cardRarityNomenclatureRepository;
    }

    private void updateCardRarityNomenclature() throws IOException, URISyntaxException {
        List<CRCardRarityNomenclature> nomenclatures = this.downloadNewCardRarities("https://royaleapi.github.io/cr-api-data/json/rarities.json");
        cardRarityNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} card rarity nomenclatures", nomenclatures.size());
    }

    private List<CRCardRarityNomenclature> downloadNewCardRarities(String stringUrl) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
    }

    private void updateCardNomenclature() throws IOException, URISyntaxException {
        List<CRCardNomenclature> nomenclatures = this.downloadNewCards("https://royaleapi.github.io/cr-api-data/json/cards_i18n.json");
        cardNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} card nomenclatures", nomenclatures.size());
    }

    private List<CRCardNomenclature> downloadNewCards(String stringUrl) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
    }

    private void updateAccountLevelNomenclature() throws IOException, URISyntaxException {
        List<CRAccountLevelNomenclature> nomenclatures = this.downloadNewAccountLevels("https://royaleapi.github.io/cr-api-data/json/exp_levels.json");
        accountLevelRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} account level nomenclatures", nomenclatures.size());
    }

    private List<CRAccountLevelNomenclature> downloadNewAccountLevels(String stringUrl) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(mapper.readTree(new URI(stringUrl).toURL()), new TypeReference<>() {});
    }

    public void updateAllNomenclatures() throws IOException, URISyntaxException {
        this.updateCardRarityNomenclature();
        this.updateCardNomenclature();
        this.updateAccountLevelNomenclature();
    }

}
