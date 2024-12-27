package com.krazytop.service.lol;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.nomenclature.lol.*;
import com.krazytop.nomenclature.riot.RIOTLanguageEnum;
import com.krazytop.repository.lol.*;
import com.krazytop.service.riot.RIOTMetadataService;
import com.krazytop.service.riot.RIOTNomenclatureService;
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
public class LOLNomenclatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LOLNomenclatureService.class);

    private final LOLPatchNomenclatureRepository patchNomenclatureRepository;
    private final RIOTNomenclatureService riotNomenclatureService;

    @Autowired
    public LOLNomenclatureService(RIOTMetadataService metadataService, LOLPatchNomenclatureRepository patchNomenclatureRepository, @Lazy RIOTNomenclatureService riotNomenclatureService) {
        this.patchNomenclatureRepository = patchNomenclatureRepository;
        this.riotNomenclatureService = riotNomenclatureService;
    }

    public void updateAllLOLNomenclatures(String patchVersion, RIOTLanguageEnum language, RIOTMetadataEntity metadata) throws IOException, URISyntaxException {
        String shortVersion = riotNomenclatureService.removeFixVersion(patchVersion);
        if (patchNomenclatureRepository.findFirstByPatchIdAndLanguage(shortVersion, language.getPath()) == null) {
            updatePatchData(patchVersion, language.getPath());
            LOLPatchNomenclature latestPatch = patchNomenclatureRepository.findLatestPatch();
            metadata.setCurrentLOLSeason(latestPatch.getSeason());
            if (!metadata.getAllLOLPatches().contains(shortVersion)) {
                metadata.getAllLOLPatches().add(shortVersion);
            }
        }
    }

    private void updatePatchData(String patchVersion, String language) throws IOException, URISyntaxException {
        String shortVersion = riotNomenclatureService.removeFixVersion(patchVersion);
        LOGGER.info("Update LOL patch {} for language {}", shortVersion, language);
        LOLPatchNomenclature patch = new LOLPatchNomenclature(shortVersion, language);
        patch.setQueues(riotNomenclatureService.getPatchQueues(riotNomenclatureService.isVersionAfterAnOther(shortVersion, "13.13") ? shortVersion : "13.14", language));
        patch.setChampions(getPatchChampions(patchVersion, language));
        patch.setSummonerSpells(getPatchSummonerSpells(patchVersion, language));
        patch.setItems(getPatchSummonerItems(patchVersion, language));
        if (riotNomenclatureService.isVersionAfterAnOther(shortVersion, "8.0")) patch.setRunes(getPatchRunes(patchVersion, language));
        if (riotNomenclatureService.isVersionAfterAnOther(shortVersion, "13.13")) patch.setAugments(getPatchAugments(shortVersion, language));
        patch.setSeason(Integer.valueOf(shortVersion.split("\\.")[0]));
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
        nomenclaturesMap.forEach((id, nomenclature) -> nomenclature.setId(id));
        return nomenclaturesMap.values().stream().toList();
    }

    private List<LOLItemNomenclature> getPatchSummonerItems(String version, String language) throws IOException, URISyntaxException {
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