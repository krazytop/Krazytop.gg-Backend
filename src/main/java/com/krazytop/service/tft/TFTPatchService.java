package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.nomenclature.riot.RIOTLanguageEnum;
import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.tft.*;
import com.krazytop.service.riot.RIOTPatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Only EUW
 */
@Service
public class TFTPatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTPatchService.class);

    private final TFTPatchNomenclatureRepository patchNomenclatureRepository;
    private final RIOTPatchService riotPatchService;

    @Autowired
    public TFTPatchService(TFTPatchNomenclatureRepository patchNomenclatureRepository, @Lazy RIOTPatchService riotPatchService) {
        this.patchNomenclatureRepository = patchNomenclatureRepository;
        this.riotPatchService = riotPatchService;
    }

    public Optional<TFTPatchNomenclature> getPatch(String patchId, String language) {
        return patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
    }

    public void updateAllTFTPatches(String patchVersion, RIOTLanguageEnum language, RIOTMetadataEntity metadata) throws IOException, URISyntaxException {
        if (getPatch(patchVersion, language.getPath()).isEmpty()) {
            updatePatchData(patchVersion, language.getPath());
            metadata.setCurrentTFTSet(patchNomenclatureRepository.findLatestPatch().getSet());
            metadata.getAllTFTPatches().add(patchVersion);
        }
    }

    private void updatePatchData(String patchVersion, String language) throws IOException, URISyntaxException {
        String uri = String.format("https://raw.communitydragon.org/%s/cdragon/tft/%s.json", patchVersion, language.toLowerCase());
        LOGGER.info("Update TFT patch {} for language {}", patchVersion, language);
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        TFTPatchNomenclature patch = new TFTPatchNomenclature(patchVersion, language);
        List<TFTItemNomenclature> itemNomenclatures = new ObjectMapper().convertValue(data.get("items"), new TypeReference<>() {});
        if (riotPatchService.isVersionAfterAnOther(patchVersion, "10.11")) {
            updateRecentPatchData(itemNomenclatures, patch, data.get("setData"));
        } else {
            updateOldPatchData(itemNomenclatures, patch, data.get("sets"));
        }
        patch.setQueues(riotPatchService.getPatchQueues(patchVersion, language));
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
        modifyImageForOldPatches(patch);
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
        if (riotPatchService.isVersionAfterAnOther(patch.getPatchId(), "13.9")) {
            patch.setItems(items);
        } else {
            patch.setItems(itemNomenclatures);
        }
        patch.setUnits(mapper.convertValue(setData.get("champions"), new TypeReference<>() {}));
        modifyImageForOldPatches(patch);
        patch.setTraits(mapper.convertValue(setData.get("traits"), new TypeReference<>() {}));
        patch.setSet(setData.get("number").asInt());
    }

    private JsonNode findCorrectRecentSetDataNode(List<JsonNode> nodes) {
        int higherSet = nodes.stream()
                .mapToInt(node -> node.get("number").asInt())
                .max()
                .orElseThrow(() -> new NoSuchElementException("No nodes available"));

        String[] prioritizedPatterns = {"TFTSet%d_Evolved", "TFTSet%d_Stage2", "TFT_Set%d_Stage2", "TFTSet%d", "TFT_Set%d"};

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

    private void modifyImageForOldPatches(TFTPatchNomenclature patch) {
        if (!riotPatchService.isVersionAfterAnOther(patch.getPatchId(), "13.9")) {
            patch.getUnits().forEach(unit -> {
                if (unit.getImage() == null && unit.getOldImage() != null) {
                    String regex = "ASSETS/UX/TFT/ChampionSplashes/([^/]+)\\.([^.]+)\\.dds";
                    String replacement = "ASSETS/Characters/$1/HUD/$1_Square.$2.tex";
                    unit.setImage(unit.getOldImage().replaceAll(regex, replacement));
                }
            });
        }
    }

}
