package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
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

    public void updateAllNomenclatures() throws IOException, URISyntaxException {
        this.updateCurrentPatchVersion();
        List<String> allPatchesVersion = this.getAllPatchesVersion();
        RIOTMetadataEntity metadata = metadataRepository.findFirstByOrderByIdAsc().orElse(new RIOTMetadataEntity());
        for (String patchVersion : allPatchesVersion) {
            for (RIOTLanguageEnum language : RIOTLanguageEnum.values()) {
                if (patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchVersion, language.getPath()) == null) {
                    this.updatePatchData(patchVersion, language.getPath());
                    TFTPatchNomenclature latestPatch = patchNomenclatureRepository.findLatestPatch();
                    metadata.setCurrentTFTSet(latestPatch.getSet());
                }
                if (!metadata.getAllPatches().contains(patchVersion)) {
                    metadata.getAllPatches().add(patchVersion);
                }
            }
        }
        metadataRepository.save(metadata);
    }

    private void updatePatchData(String patchVersion, String language) throws IOException, URISyntaxException {
        String uri = String.format("https://raw.communitydragon.org/%s/cdragon/tft/%s.json", patchVersion, language);
        LOGGER.info("Update TFT patch {} for language {}", patchVersion, language);
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        TFTPatchNomenclature patch = new TFTPatchNomenclature(patchVersion, language);
        List<TFTItemNomenclature> itemNomenclatures = new ObjectMapper().convertValue(data.get("items"), new TypeReference<>() {});
        if (isVersionAfterAnOther(patchVersion, "10.11")) {
            updateRecentPatchData(itemNomenclatures, patch, data.get("setData"));
        } else {
            updateOldPatchData(itemNomenclatures, patch, data.get("sets"));
        }
        patch.setQueues(this.deserializeQueues(patchVersion, language));
        patchNomenclatureRepository.save(patch);
    }

    private void updateOldPatchData(List<TFTItemNomenclature> itemNomenclatures, TFTPatchNomenclature patch, JsonNode data) {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, JsonNode> dataMap = mapper.convertValue(data, new TypeReference<>() {});
        Map.Entry<Integer, JsonNode> mostRecentSet = dataMap.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .orElseThrow(() -> new NoSuchElementException("No entries found in the map"));
        patch.setSet(mostRecentSet.getKey());
        patch.setAugments(List.of());
        patch.setTraits(mapper.convertValue(mostRecentSet.getValue().get("augments"), new TypeReference<>() {}));
        patch.setUnits(mapper.convertValue(mostRecentSet.getValue().get("champions"), new TypeReference<>() {}));
        patch.setItems(itemNomenclatures);
    }

    private void updateRecentPatchData(List<TFTItemNomenclature> itemNomenclatures, TFTPatchNomenclature patch, JsonNode data) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode setData = findCorrectRecentSetDataNode(mapper.convertValue(data, new TypeReference<>() {}));
        List<TFTItemNomenclature> augments = Optional.ofNullable(mapper.convertValue(setData.get("augments"), new TypeReference<List<String>>() {}))
                .orElse(List.of())
                .stream()
                .map(augmentId -> itemNomenclatures.stream()
                        .filter(item -> Objects.equals(item.getId(), augmentId))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Augment not found: " + augmentId)))
                .toList();
        List<TFTItemNomenclature> items = Optional.ofNullable(mapper.convertValue(setData.get("items"), new TypeReference<List<String>>() {}))
                .orElse(List.of())
                .stream()
                .map(itemId -> itemNomenclatures.stream()
                        .filter(item -> Objects.equals(item.getId(), itemId))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("Item not found: " + itemId)))
                .toList();
        patch.setAugments(augments);
        patch.setItems(items);
        patch.setUnits(mapper.convertValue(setData.get("champions"), new TypeReference<>() {}));
        patch.setTraits(mapper.convertValue(setData.get("traits"), new TypeReference<>() {}));
        patch.setSet(setData.get("number").asInt());
    }

    private List<TFTQueueNomenclature> deserializeQueues(String patchVersion, String language) throws IOException, URISyntaxException {
        if (Objects.equals(patchVersion, "13.2") || Objects.equals(patchVersion, "13.3")) patchVersion = "13.4";
        if (Objects.equals(patchVersion, "11.7")) patchVersion = "11.8";
        ObjectMapper mapper = new ObjectMapper();
        String queueUri = String.format("https://raw.communitydragon.org/%s/plugins/rcp-be-lol-game-data/global/%s/v1/queues.json", patchVersion, language);
        if (isVersionAfterAnOther(patchVersion, "14.12")) {
            return mapper.convertValue(mapper.convertValue(new ObjectMapper().readTree(new URI(queueUri).toURL()), new TypeReference<>() {}), new TypeReference<>() {});
        } else {
            Map<String, TFTQueueNomenclature> nomenclaturesMap = mapper.convertValue(new ObjectMapper().readTree(new URI(queueUri).toURL()), new TypeReference<>() {});
            nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
            return nomenclaturesMap.values().stream().toList();
        }
    }

    private JsonNode findCorrectRecentSetDataNode(List<JsonNode> nodes) {
        int higherSet = nodes.stream()
                .mapToInt(node -> node.get("number").asInt())
                .max()
                .orElseThrow(() -> new NoSuchElementException("No nodes available"));

        String[] prioritizedPatterns = {"TFTSet%d_Stage2", "TFT_Set%d_Stage2", "TFTSet%d", "TFT_Set%d"};

        for (String pattern : prioritizedPatterns) {
            String mutator = String.format(pattern, higherSet);
            Optional<JsonNode> node = nodes.stream()
                    .filter(n -> Objects.equals(n.get("mutator").asText(), mutator))
                    .findFirst();
            if (node.isPresent()) {
                return node.get();
            }
        }
        throw new NoSuchElementException(String.format("Data node is not found for set %d", higherSet));
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
                .filter(version -> isVersionAfterAnOther(version, "9.13"))
                .distinct()
                .toList();
        return allPatchesVersion;
    }

    private boolean isVersionAfterAnOther(String version, String referentVersion) {
        String[] v1 = version.split("\\.");
        String[] v2 = referentVersion.split("\\.");

        int majorDiff = Integer.parseInt(v1[0]) - Integer.parseInt(v2[0]);
        return majorDiff != 0 ? majorDiff > 0 : Integer.parseInt(v1[1]) > Integer.parseInt(v2[1]);
    }

}
