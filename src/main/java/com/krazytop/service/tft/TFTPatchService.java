package com.krazytop.service.tft;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.LanguageDTO;
import com.krazytop.api_gateway.model.generated.TFTPatchDTO;
import com.krazytop.entity.tft.TFTMetadata;
import com.krazytop.exception.CustomException;
import com.krazytop.exception.ApiErrorEnum;
import com.krazytop.mapper.tft.TFTPatchMapper;
import com.krazytop.nomenclature.riot.RIOTQueueNomenclature;
import com.krazytop.nomenclature.tft.*;
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

import static com.krazytop.nomenclature.LanguageService.SUPPORTED_LANGUAGES;

@Service
public class TFTPatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TFTPatchService.class);

    private final TFTPatchRepository patchNomenclatureRepository;
    private final TFTMetadataService metadataService;
    private final TFTPatchMapper patchMapper;

    @Autowired
    public TFTPatchService(TFTPatchRepository patchNomenclatureRepository, TFTMetadataService metadataService, TFTPatchMapper patchMapper) {
        this.patchNomenclatureRepository = patchNomenclatureRepository;
        this.metadataService = metadataService;
        this.patchMapper = patchMapper;
    }

    public Optional<TFTPatch> getPatch(String patchId, String language) {
        return patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
    }

    public TFTPatchDTO getPatchDTO(String patchId, String language) {
        return patchMapper.toDTO(getPatch(patchId, language).orElseThrow(() -> new CustomException(ApiErrorEnum.PATCH_NOT_FOUND)));
    }

    public void updateAllPatches() throws IOException, URISyntaxException {
        updateCurrentPatchVersion();
        List<String> allPatchesVersion = getAllPatchesVersion().stream()
                .map(this::removeFixVersion)
                .filter(v -> isVersionAfterAnOther(v, "9.13"))
                .toList();
        TFTMetadata metadata = metadataService.getMetadata().orElse(new TFTMetadata());
        for (String patchVersion : allPatchesVersion) {
            for (String language : SUPPORTED_LANGUAGES.stream().map(LanguageDTO::getRiotPatchPath).toList()) {
                if (getPatch(patchVersion, language).isEmpty()) {
                    updatePatchData(patchVersion, language);
                }
            }
            metadata.getAllPatches().add(patchVersion);
            metadata.setCurrentSet(patchNomenclatureRepository.findLatestPatch().getSet());
            metadataService.saveMetadata(metadata);
        }
    }

    public void updateCurrentPatchVersion() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        TFTMetadata metadata = metadataService.getMetadata().orElse(new TFTMetadata());
        String currentPatch = new ObjectMapper().readTree(url).get("v").asText().replaceAll("^(\\d+\\.\\d+).*", "$1");
        if (!Objects.equals(metadata.getCurrentPatch(), currentPatch)) {
            metadata.setCurrentPatch(currentPatch);
            metadataService.saveMetadata(metadata);
        }
    }

    public List<String> getAllPatchesVersion() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String uri = "https://ddragon.leagueoflegends.com/api/versions.json";
        JsonNode data = mapper.readTree(new URI(uri).toURL());
        List<String> allPatchesVersion = mapper.convertValue(data, new TypeReference<>() {});
        return allPatchesVersion.stream()
                .filter(version -> !version.contains("lol"))
                .filter(version -> !version.startsWith("0."))
                .toList();
    }

    private void updatePatchData(String patchVersion, String language) throws IOException, URISyntaxException {
        String uri = String.format("https://raw.communitydragon.org/%s/cdragon/tft/%s.json", patchVersion, language.toLowerCase());
        LOGGER.info("Update TFT patch {} for language {}", patchVersion, language);
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        TFTPatch patch = new TFTPatch(patchVersion, language);
        List<TFTItemNomenclature> itemNomenclatures = new ObjectMapper().convertValue(data.get("items"), new TypeReference<>() {});
        if (isVersionAfterAnOther(patchVersion, "10.11")) {
            updateRecentPatchData(itemNomenclatures, patch, data.get("setData"));
        } else {
            updateOldPatchData(itemNomenclatures, patch, data.get("sets"));
        }
        patch.setQueues(getPatchQueues(patchVersion, language));
        patchNomenclatureRepository.save(patch);
    }

    private void updateOldPatchData(List<TFTItemNomenclature> itemNomenclatures, TFTPatch patch, JsonNode data) {
        ObjectMapper mapper = new ObjectMapper();
        Map<Integer, JsonNode> dataMap = mapper.convertValue(data, new TypeReference<>() {});
        Map.Entry<Integer, JsonNode> mostRecentSet = dataMap.entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .orElseThrow(() -> new NoSuchElementException("No entries found in the map"));//TODO custom exception
        patch.setSet(mostRecentSet.getKey());
        patch.setAugments(List.of());
        patch.setTraits(mapper.convertValue(mostRecentSet.getValue().get("augments"), new TypeReference<>() {}));
        patch.setUnits(mapper.convertValue(mostRecentSet.getValue().get("champions"), new TypeReference<>() {}));
        modifyImageForOldPatches(patch);
        patch.setItems(itemNomenclatures);
    }

    private void updateRecentPatchData(List<TFTItemNomenclature> itemNomenclatures, TFTPatch patch, JsonNode data) {
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
        if (isVersionAfterAnOther(patch.getPatchId(), "13.9")) {
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

    private void modifyImageForOldPatches(TFTPatch patch) {
        if (!isVersionAfterAnOther(patch.getPatchId(), "13.9")) {
            patch.getUnits().forEach(unit -> {
                if (unit.getImage() == null && unit.getOldImage() != null) {
                    String regex = "ASSETS/UX/TFT/ChampionSplashes/([^/]+)\\.([^.]+)\\.dds";
                    String replacement = "ASSETS/Characters/$1/HUD/$1_Square.$2.tex";
                    unit.setImage(unit.getOldImage().replaceAll(regex, replacement));
                }
            });
        }
    }

    public List<RIOTQueueNomenclature> getPatchQueues(String patchVersion, String language) throws IOException, URISyntaxException {
        if (Objects.equals(patchVersion, "13.2") || Objects.equals(patchVersion, "13.3")) patchVersion = "13.4";
        if (Objects.equals(patchVersion, "11.7")) patchVersion = "11.8";
        ObjectMapper mapper = new ObjectMapper();
        String queueUri = String.format("https://raw.communitydragon.org/%s/plugins/rcp-be-lol-game-data/global/%s/v1/queues.json", patchVersion, language.toLowerCase());
        if (isVersionAfterAnOther(patchVersion, "14.12")) {
            return mapper.convertValue(mapper.convertValue(new ObjectMapper().readTree(new URI(queueUri).toURL()), new TypeReference<>() {}), new TypeReference<>() {});
        } else {
            Map<String, RIOTQueueNomenclature> nomenclaturesMap = mapper.convertValue(new ObjectMapper().readTree(new URI(queueUri).toURL()), new TypeReference<>() {});
            nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
            return nomenclaturesMap.values().stream().toList();
        }
    }

    public String removeFixVersion(String version) {
        return version.substring(0, version.lastIndexOf('.'));
    }

    public boolean isVersionAfterAnOther(String version, String referentVersion) {
        String[] v1 = version.split("\\.");
        String[] v2 = referentVersion.split("\\.");

        int majorDiff = Integer.parseInt(v1[0]) - Integer.parseInt(v2[0]);
        return majorDiff != 0 ? majorDiff > 0 : Integer.parseInt(v1[1]) > Integer.parseInt(v2[1]);
    }

}
