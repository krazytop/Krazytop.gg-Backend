package com.krazytop.service.clash_royal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import com.krazytop.nomenclature.clash_royal.CRArenaNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import com.krazytop.repository.clash_royal.CRAccountLevelNomenclatureRepository;
import com.krazytop.repository.clash_royal.CRArenaNomenclatureRepository;
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
    private final CRArenaNomenclatureRepository arenaNomenclatureRepository;

    @Autowired
    public CRNomenclatureService(CRAccountLevelNomenclatureRepository accountLevelRepository, CRCardNomenclatureRepository cardNomenclatureRepository, CRCardRarityNomenclatureRepository cardRarityNomenclatureRepository, CRArenaNomenclatureRepository arenaNomenclatureRepository) {
        this.accountLevelRepository = accountLevelRepository;
        this.cardNomenclatureRepository = cardNomenclatureRepository;
        this.cardRarityNomenclatureRepository = cardRarityNomenclatureRepository;
        this.arenaNomenclatureRepository = arenaNomenclatureRepository;
    }

    private void updateCardRarityNomenclature() throws IOException, URISyntaxException {
        String url = "https://royaleapi.github.io/cr-api-data/json/rarities.json";
        ObjectMapper mapper = new ObjectMapper();
        List<CRCardRarityNomenclature> nomenclatures = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
        cardRarityNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} card rarity nomenclatures", nomenclatures.size());
    }

    private void updateCardNomenclature() throws IOException, URISyntaxException {
        String url = "https://royaleapi.github.io/cr-api-data/json/cards_i18n.json";
        ObjectMapper mapper = new ObjectMapper();
        List<CRCardNomenclature> nomenclatures = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
        cardNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} card nomenclatures", nomenclatures.size());
    }

    private void updateAccountLevelNomenclature() throws IOException, URISyntaxException {
        String url = "https://royaleapi.github.io/cr-api-data/json/exp_levels.json";
        ObjectMapper mapper = new ObjectMapper();
        List<CRAccountLevelNomenclature> nomenclatures = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
        accountLevelRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} account level nomenclatures", nomenclatures.size());
    }

    private void updateArenaNomenclature() throws IOException, URISyntaxException {
        String url = "https://royaleapi.github.io/cr-api-data/json/arenas.json";
        ObjectMapper mapper = new ObjectMapper();
        List<CRArenaNomenclature> nomenclatures = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
        arenaNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} arena nomenclatures", nomenclatures.size());
    }

    public void updateAllNomenclatures() throws IOException, URISyntaxException {
        this.updateCardRarityNomenclature();
        this.updateCardNomenclature();
        this.updateAccountLevelNomenclature();
        this.updateArenaNomenclature();
    }

}
