package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.nomenclature.lol.LOLItemNomenclature;
import com.krazytop.nomenclature.riot.RIOTLanguageEnum;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import com.krazytop.repository.tft.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static java.util.Collections.max;

/**
 * Only EUW
 */
@Service
public class TFTNomenclatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTNomenclatureService.class);

    private final TFTPatchNomenclatureRepository patchNomenclatureRepository;
    private final RIOTMetadataRepository metadataRepository;

    @Autowired
    public TFTNomenclatureService(TFTPatchNomenclatureRepository patchNomenclatureRepository, RIOTMetadataRepository metadataRepository) {
        this.patchNomenclatureRepository = patchNomenclatureRepository;
        this.metadataRepository = metadataRepository;
    }

    public boolean updateAllNomenclatures() throws IOException, URISyntaxException {
        this.updateCurrentPatchVersion();
        List<String> allPatchesVersion = this.getAllPatchesVersion();
        RIOTMetadataEntity metadata = metadataRepository.findFirstByOrderByIdAsc().orElse(new RIOTMetadataEntity());
        boolean patchesUpdated = false;
        for (String patchVersion : allPatchesVersion) {
            for (RIOTLanguageEnum language : RIOTLanguageEnum.values()) {
                if (patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchVersion, language.getPath()) == null) {
                    this.updatePatchData(patchVersion, language.getPath());
                    patchesUpdated = true;
                }
                if (!metadata.getAllPatches().contains(patchVersion)) {
                    metadata.getAllPatches().add(patchVersion);
                    metadataRepository.save(metadata);
                }
            }
        }
        return patchesUpdated;
    }

    private void updatePatchData(String patchVersion, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String uri = String.format("https://raw.communitydragon.org/%s/cdragon/tft/%s.json", patchVersion, language);
        LOGGER.info("Update TFT patch {} for language {}", patchVersion, language);
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        List<TFTItemNomenclature> itemNomenclatures = mapper.convertValue(data.get("items"), new TypeReference<>() {});
        JsonNode setData = findCorrectSetData(mapper.convertValue(data.get("setData"), new TypeReference<>() {}));
        TFTPatchNomenclature patch = new TFTPatchNomenclature(patchVersion, language);
        List<TFTItemNomenclature> augments = mapper.convertValue(setData.get("augments"), new TypeReference<List<String>>() {}).stream()
                .map(augmentId -> itemNomenclatures.stream()
                        .filter(item -> Objects.equals(item.getId(), augmentId))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Augment not found: " + augmentId)))
                .toList();
        List<TFTItemNomenclature> items = mapper.convertValue(setData.get("items"), new TypeReference<List<String>>() {}).stream()
                .map(itemId -> itemNomenclatures.stream()
                        .filter(item -> Objects.equals(item.getId(), itemId))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Item not found: " + itemId)))
                .toList();
        patch.setAugments(augments);
        patch.setItems(items);
        patch.setUnits(mapper.convertValue(setData.get("champions"), new TypeReference<>() {}));
        patch.setTraits(mapper.convertValue(setData.get("traits"), new TypeReference<>() {}));
        patch.setQueues(this.deserializeQueues(patchVersion, language));
        this.patchNomenclatureRepository.save(patch);
    }

    private List<TFTQueueNomenclature> deserializeQueues(String patchVersion, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String queueUri = String.format("https://raw.communitydragon.org/%s/plugins/rcp-be-lol-game-data/global/%s/v1/queues.json", patchVersion, language);
        if (isAfter(patchVersion, "14.12")) {
            return mapper.convertValue(mapper.convertValue(new ObjectMapper().readTree(new URI(queueUri).toURL()), new TypeReference<>() {}), new TypeReference<>() {});
        } else {
            Map<String, TFTQueueNomenclature> nomenclaturesMap = mapper.convertValue(new ObjectMapper().readTree(new URI(queueUri).toURL()), new TypeReference<>() {});
            nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
            return nomenclaturesMap.values().stream().toList();
        }
    }

    private JsonNode findCorrectSetData(List<JsonNode> nodes) {
        int higherSet = max(nodes.stream().map(node -> node.get("number").asInt()).toList());
        Optional<JsonNode> midSetNode = nodes.stream().filter(node -> Objects.equals(node.get("mutator").asText(), String.format("TFTSet%d_Stage2", higherSet))).findFirst();
        Optional<JsonNode> setNode = nodes.stream().filter(node -> Objects.equals(node.get("mutator").asText(), String.format("TFTSet%d", higherSet))).findFirst();
        if (midSetNode.isPresent()) {
            return midSetNode.get();
        } else if (setNode.isPresent()) {
            return setNode.get();
        } else {
            throw new RuntimeException();//TODO
        }
    }

    private Integer updateSetData(String version) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String uri = String.format("https://raw.communitydragon.org/%s/cdragon/tft/fr_fr.json", version);
        LOGGER.info("Update TFT set {}", version);
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        Map<String, JsonNode> sets = mapper.convertValue(data.get("sets"), new TypeReference<>() {});
        List<Integer> setNbs = new ArrayList<>();
        sets.forEach((setNb, setData) -> {
            setNbs.add(Integer.valueOf(setNb));
        });
        return max(setNbs);
    }

    private void updateCurrentPatchVersion() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        RIOTMetadataEntity metadata = metadataRepository.findFirstByOrderByIdAsc().orElse(new RIOTMetadataEntity());
        String currentPatch = new ObjectMapper().readTree(url).get("v").asText();
        if (!Objects.equals(metadata.getCurrentPatch(), currentPatch)) {
            metadata.setCurrentPatch(currentPatch);
            metadataRepository.save(metadata);
        }
    }

    private List<String> getAllPatchesVersion() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String uri = "https://ddragon.leagueoflegends.com/api/versions.json";
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        List<String> allPatchesVersion = mapper.convertValue(data, new TypeReference<>() {});
        allPatchesVersion = allPatchesVersion.stream()
                .filter(version -> !version.contains("lol"))
                .filter(version -> !version.startsWith("0."))
                .map(version -> version.substring(0, version.lastIndexOf('.')))
                .filter(version -> isAfter(version, "13.10"))
                .distinct()
                .toList();
        return allPatchesVersion;
    }

    private boolean isAfter(String version, String referentVersion) {//TODO
        String[] parts1 = version.split("\\.");
        String[] parts2 = referentVersion.split("\\.");

        int majorVersion = Integer.parseInt(parts1[0]);
        int majorReferentVersion = Integer.parseInt(parts2[0]);
        int minorVersion = Integer.parseInt(parts1[1]);
        int minorReferentVersion = Integer.parseInt(parts2[1]);

        if (majorVersion != majorReferentVersion) {
            return majorVersion > majorReferentVersion;
        } else {
            return minorVersion > minorReferentVersion;
        }
    }

}
