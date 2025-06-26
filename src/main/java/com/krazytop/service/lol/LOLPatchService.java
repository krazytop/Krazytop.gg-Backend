package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.api_gateway.model.generated.LanguageDTO;
import com.krazytop.entity.lol.LOLMetadata;
import com.krazytop.entity.tft.TFTMetadata;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.nomenclature.riot.RIOTQueueNomenclature;
import com.krazytop.repository.lol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.krazytop.nomenclature.LanguageService.SUPPORTED_LANGUAGES;

@Service
public class LOLPatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLPatchService.class);

    private final LOLPatchRepository patchNomenclatureRepository;
    private final LOLMetadataService metadataService;

    @Autowired
    public LOLPatchService(LOLPatchRepository patchNomenclatureRepository, LOLMetadataService metadataService) {
        this.patchNomenclatureRepository = patchNomenclatureRepository;
        this.metadataService = metadataService;
    }

    public Optional<LOLPatch> getPatch(String patchId, String language) {
        return patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
    }

    public void updateAllPatches() throws IOException, URISyntaxException {
        List<String> allPatchesVersion = getAllPatchesVersion().stream()
                .map(this::removeFixVersion)
                .toList();
        LOLMetadata metadata = metadataService.getMetadata().orElse(new LOLMetadata());
        for (String patchVersion : allPatchesVersion) {
            for (String language : SUPPORTED_LANGUAGES.stream().map(LanguageDTO::getRiotPatchPath).toList()) {
                if (getPatch(patchVersion, language).isEmpty()) {
                    updatePatchData(patchVersion, language);
                }
            }
            metadata.getAllPatches().add(patchVersion);
            metadata.setCurrentSeason(patchNomenclatureRepository.findLatestPatch().getSeason());
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
        String shortVersion = removeFixVersion(patchVersion);
        LOGGER.info("Update LOL patch {} for language {}", shortVersion, language);
        LOLPatch patch = new LOLPatch(shortVersion, language);
        patch.setChampions(getPatchChampions(patchVersion, language));
        patch.setSummonerSpells(getPatchSummonerSpells(patchVersion, language));
        patch.setItems(getPatchItems(patchVersion, language));
        if (isVersionAfterAnOther(shortVersion, "8.0")) patch.setRunes(getPatchRunes(patchVersion, language));
        if (isVersionAfterAnOther(shortVersion, "13.13")) patch.setAugments(getPatchAugments(shortVersion, language));
        patch.setSeason(Integer.valueOf(shortVersion.split("\\.")[0]));
        patch.setQueues(getPatchQueues(isVersionAfterAnOther(shortVersion, "13.13") ? shortVersion : "13.14", language));
        patchNomenclatureRepository.save(patch);
    }

    private List<LOLChampionNomenclature> getPatchChampions(String version, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/%s/champion.json", version, language);
        Map<String, LOLChampionNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        return nomenclaturesMap.values().stream().toList();
    }

    private List<LOLSummonerSpellNomenclature> getPatchSummonerSpells(String version, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/%s/summoner.json", version, language);
        Map<String, LOLSummonerSpellNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        return nomenclaturesMap.values().stream().toList();
    }

    private List<LOLItemNomenclature> getPatchItems(String version, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/%s/item.json", version, language);
        Map<String, LOLItemNomenclature> nomenclaturesMap = mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("data"), new TypeReference<>() {});
        nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
        return nomenclaturesMap.values().stream().toList();
    }

    private List<LOLAugmentNomenclature> getPatchAugments(String version, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://raw.communitydragon.org/%s/cdragon/arena/%s.json", version, language.toLowerCase());
        return mapper.convertValue(mapper.readTree(new URI(url).toURL()).get("augments"), new TypeReference<>() {});
    }

    private List<LOLRuneNomenclature> getPatchRunes(String version, String language) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://ddragon.leagueoflegends.com/cdn/%s/data/%s/runesReforged.json", version, language);
        return mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
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