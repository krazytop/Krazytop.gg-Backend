package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.tft.TFTVersionEntity;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.tft.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Only EUW & fr_FR for now
 */
@Service
public class TFTNomenclatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTNomenclatureService.class);

    private final TFTTraitNomenclatureRepository traitNomenclatureRepository;
    private final TFTUnitNomenclatureRepository unitNomenclatureRepository;
    private final TFTQueueNomenclatureRepository queueNomenclatureRepository;
    private final TFTItemNomenclatureRepository itemNomenclatureRepository;
    private final TFTVersionRepository versionRepository;

    @Autowired
    public TFTNomenclatureService(TFTTraitNomenclatureRepository traitNomenclatureRepository, TFTUnitNomenclatureRepository unitNomenclatureRepository, TFTQueueNomenclatureRepository queueNomenclatureRepository, TFTItemNomenclatureRepository itemNomenclatureRepository, TFTVersionRepository versionRepository) {
        this.traitNomenclatureRepository = traitNomenclatureRepository;
        this.unitNomenclatureRepository = unitNomenclatureRepository;
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.versionRepository = versionRepository;
    }

    private void updateItemNomenclature(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List<TFTItemNomenclature> nomenclatures = mapper.convertValue(node, new TypeReference<>() {});
        itemNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} TFT item nomenclatures", nomenclatures.size());
    }

    private void updateQueueNomenclature(String version) throws URISyntaxException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/tft-queues.json", version);
        Map<String, TFTQueueNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        List<TFTQueueNomenclature> nomenclatures = nomenclaturesMap.values().stream().toList();
        queueNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} TFT queue nomenclatures", nomenclatures.size());
    }

    private void updateUnitNomenclature(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List<TFTUnitNomenclature> nomenclatures = mapper.convertValue(node, new TypeReference<>() {});
        unitNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} TFT unit nomenclatures", nomenclatures.size());
    }

    private void updateTraitNomenclature(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List<TFTTraitNomenclature> nomenclatures = mapper.convertValue(node, new TypeReference<>() {});
        traitNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} TFT trait nomenclatures", nomenclatures.size());
    }

    public boolean updateAllNomenclatures() throws IOException, URISyntaxException {
        String lastOfficialVersion = this.getLastOfficialNomenclatureVersion();
        String lastCommunityVersion = this.getLastCommunityNomenclatureVersion();
        TFTVersionEntity dbVersion = this.versionRepository.findFirstByOrderByOfficialVersionAsc();

        if (dbVersion == null ||
                !Objects.equals(lastOfficialVersion, dbVersion.getOfficialVersion()) ||
                !Objects.equals(lastCommunityVersion, dbVersion.getCommunityVersion())) {
            if (dbVersion == null || !Objects.equals(lastOfficialVersion, dbVersion.getOfficialVersion())) {
                this.updateQueueNomenclature(lastOfficialVersion);
            }
            if (dbVersion == null || !Objects.equals(lastCommunityVersion, dbVersion.getCommunityVersion())) {
                this.updateSetData("latest");
            }
            this.versionRepository.save(new TFTVersionEntity(lastCommunityVersion, lastOfficialVersion));
            return true;
        } else {
            return false;
        }
    }

    private void updateSetData(String version) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String uri = String.format("https://raw.communitydragon.org/%s/cdragon/tft/fr_fr.json", version);
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        this.updateItemNomenclature(data.get("items"));
        Map<String, JsonNode> sets = mapper.convertValue(data.get("sets"), new TypeReference<>() {});
        sets.values().forEach(set -> {
            this.updateTraitNomenclature(set.get("traits"));
            this.updateUnitNomenclature(set.get("champions"));
        });
    }

    private String getLastOfficialNomenclatureVersion() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        return new ObjectMapper().readTree(url).get("v").asText();
    }

    private String getLastCommunityNomenclatureVersion() throws IOException, URISyntaxException {
        String uri = "https://raw.communitydragon.org/latest/content-metadata.json";
        URL url = new URI(uri).toURL();
        return new ObjectMapper().readTree(url).get("version").asText();
    }

    public void updateLegacyNomenclatures() throws IOException, URISyntaxException {
        List<String> legacyVersions = List.of("14.5", "13.11");
        for (String version : legacyVersions) {
            this.updateSetData(version);
        }
    }
}
