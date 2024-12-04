package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.tft.TFTVersionEntity;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.tft.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    private static final List<String> VERSIONS = List.of("set8", "set8_5", "set9", "set9_5");
    private static final String FOLDER = "/src/main/resources/data/tft/";

    private final TFTTraitNomenclatureRepository traitNomenclatureRepository;
    private final TFTUnitNomenclatureRepository unitNomenclatureRepository;
    private final TFTQueueNomenclatureRepository queueNomenclatureRepository;
    private final TFTItemNomenclatureRepository itemNomenclatureRepository;
    private final TFTAugmentNomenclatureRepository augmentNomenclatureRepository;
    private final TFTVersionRepository versionRepository;

    @Autowired
    public TFTNomenclatureService(TFTTraitNomenclatureRepository traitNomenclatureRepository, TFTUnitNomenclatureRepository unitNomenclatureRepository, TFTQueueNomenclatureRepository queueNomenclatureRepository, TFTItemNomenclatureRepository itemNomenclatureRepository, TFTAugmentNomenclatureRepository augmentNomenclatureRepository, TFTVersionRepository versionRepository) {
        this.traitNomenclatureRepository = traitNomenclatureRepository;
        this.unitNomenclatureRepository = unitNomenclatureRepository;
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.augmentNomenclatureRepository = augmentNomenclatureRepository;
        this.versionRepository = versionRepository;
    }

    public boolean updateUnitNomenclature() {
        unitNomenclatureRepository.deleteAll();
        try {
            for (String version : VERSIONS) {
                ObjectMapper objectMapper = new ObjectMapper();
                File itemFile = new File("getCurrentWorkingDirectory()" + FOLDER + version + "/tft-units.json");
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

    public void updateQueueNomenclature(String version) throws URISyntaxException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/fr_FR/tft-queues.json", version);
        Map<String, TFTQueueNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        List<TFTQueueNomenclature> nomenclatures = nomenclaturesMap.values().stream().toList();
        queueNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} TFT queue nomenclatures", nomenclatures.size());
    }

    public void updateItemNomenclature(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        List<TFTItemNomenclature> nomenclatures = mapper.convertValue(node, new TypeReference<>() {});
        itemNomenclatureRepository.saveAll(nomenclatures);
        LOGGER.info("Update {} TFT item nomenclatures", nomenclatures.size());
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
                String uri = "https://raw.communitydragon.org/latest/cdragon/tft/fr_fr.json";
                URL url = new URI(uri).toURL();
                JsonNode json = new ObjectMapper().readTree(url);
                this.updateItemNomenclature(json.get("items"));
            }
            this.versionRepository.save(new TFTVersionEntity(lastCommunityVersion, lastOfficialVersion));
            return true;
        } else {
            return false;
        }
    }

    private String getLastOfficialNomenclatureVersion() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        return new ObjectMapper().readTree(url).get("v").asText();
    }

    private String getLastCommunityNomenclatureVersion() throws IOException, URISyntaxException {// 12.22 premiere version
        String uri = "https://raw.communitydragon.org/latest/content-metadata.json";
        URL url = new URI(uri).toURL();
        return new ObjectMapper().readTree(url).get("version").asText();
    }
}
