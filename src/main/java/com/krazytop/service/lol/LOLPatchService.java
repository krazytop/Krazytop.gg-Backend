package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.nomenclature.riot.RIOTLanguageEnum;
import com.krazytop.repository.lol.*;
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
public class LOLPatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLPatchService.class);

    private final LOLPatchNomenclatureRepository patchNomenclatureRepository;
    private final RIOTPatchService riotPatchService;

    @Autowired
    public LOLPatchService(LOLPatchNomenclatureRepository patchNomenclatureRepository, @Lazy RIOTPatchService riotPatchService) {
        this.patchNomenclatureRepository = patchNomenclatureRepository;
        this.riotPatchService = riotPatchService;
    }

    public Optional<LOLPatchNomenclature> getPatch(String patchId, String language) {
        return patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
    }

    public void updateAllLOLPatches(String patchVersion, RIOTLanguageEnum language, RIOTMetadataEntity metadata) throws IOException, URISyntaxException {
        String shortVersion = riotPatchService.removeFixVersion(patchVersion);
        if (patchNomenclatureRepository.findFirstByPatchIdAndLanguage(shortVersion, language.getPath()).isEmpty()) {
            updatePatchData(patchVersion, language.getPath());
            LOLPatchNomenclature latestPatch = patchNomenclatureRepository.findLatestPatch();
            metadata.setCurrentLOLSeason(latestPatch.getSeason());
            if (!metadata.getAllLOLPatches().contains(shortVersion)) {
                metadata.getAllLOLPatches().add(shortVersion);
            }
        }
    }

    private void updatePatchData(String patchVersion, String language) throws IOException, URISyntaxException {
        String shortVersion = riotPatchService.removeFixVersion(patchVersion);
        LOGGER.info("Update LOL patch {} for language {}", shortVersion, language);
        LOLPatchNomenclature patch = new LOLPatchNomenclature(shortVersion, language);
        patch.setChampions(getPatchChampions(patchVersion, language));
        patch.setSummonerSpells(getPatchSummonerSpells(patchVersion, language));
        patch.setItems(getPatchItems(patchVersion, language));
        if (riotPatchService.isVersionAfterAnOther(shortVersion, "8.0")) patch.setRunes(getPatchRunes(patchVersion, language));
        if (riotPatchService.isVersionAfterAnOther(shortVersion, "13.13")) patch.setAugments(getPatchAugments(shortVersion, language));
        patch.setSeason(Integer.valueOf(shortVersion.split("\\.")[0]));
        patch.setQueues(riotPatchService.getPatchQueues(riotPatchService.isVersionAfterAnOther(shortVersion, "13.13") ? shortVersion : "13.14", language));
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

}