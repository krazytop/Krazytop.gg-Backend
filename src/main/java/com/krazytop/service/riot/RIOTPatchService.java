package com.krazytop.service.riot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.nomenclature.lol.LOLPatchNomenclature;
import com.krazytop.nomenclature.riot.RIOTLanguageEnum;
import com.krazytop.nomenclature.riot.RIOTQueueNomenclature;
import com.krazytop.nomenclature.tft.TFTPatchNomenclature;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import com.krazytop.service.lol.LOLPatchService;
import com.krazytop.service.tft.TFTPatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Only EUW
 */
@Service
public class RIOTPatchService {

    private final RIOTMetadataRepository metadataRepository;
    private final TFTPatchService tftPatchService;
    private final LOLPatchService lolPatchService;

    @Autowired
    public RIOTPatchService(RIOTMetadataRepository metadataRepository, TFTPatchService tftPatchService, LOLPatchService lolPatchService) {
        this.metadataRepository = metadataRepository;
        this.tftPatchService = tftPatchService;
        this.lolPatchService = lolPatchService;
    }

    public Optional<LOLPatchNomenclature> getLOLPatch(String patchId, String language) {
        return lolPatchService.getPatch(patchId, language);
    }

    public Optional<TFTPatchNomenclature> getTFTPatch(String patchId, String language) {
        return tftPatchService.getPatch(patchId, language);
    }

    public void updateAllPatches() throws IOException, URISyntaxException {
        updateCurrentPatchVersion();
        List<String> allPatchesVersion = getAllPatchesVersion();
        RIOTMetadataEntity metadata = metadataRepository.findFirstByOrderByIdAsc().orElse(new RIOTMetadataEntity());
        for (String patchVersion : allPatchesVersion) {
            for (RIOTLanguageEnum language : RIOTLanguageEnum.values()) {
                lolPatchService.updateAllLOLPatches(patchVersion, language, metadata);
                if (isVersionAfterAnOther(removeFixVersion(patchVersion), "9.13")) {
                    tftPatchService.updateAllTFTPatches(removeFixVersion(patchVersion), language, metadata);
                }
            }
            metadataRepository.save(metadata);
        }
    }

    public void updateCurrentPatchVersion() throws IOException, URISyntaxException {
        String uri = "https://ddragon.leagueoflegends.com/realms/euw.json";
        URL url = new URI(uri).toURL();
        RIOTMetadataEntity metadata = metadataRepository.findFirstByOrderByIdAsc().orElse(new RIOTMetadataEntity());
        String currentPatch = new ObjectMapper().readTree(url).get("v").asText().replaceAll("^(\\d+\\.\\d+).*", "$1");
        if (!Objects.equals(metadata.getCurrentPatch(), currentPatch)) {
            metadata.setCurrentPatch(currentPatch);
            metadataRepository.save(metadata);
        }
    }

    public List<String> getAllPatchesVersion() throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        String uri = "https://ddragon.leagueoflegends.com/api/versions.json";
        JsonNode data = new ObjectMapper().readTree(new URI(uri).toURL());
        List<String> allPatchesVersion = mapper.convertValue(data, new TypeReference<>() {});
        allPatchesVersion = allPatchesVersion.stream()
                .filter(version -> !version.contains("lol"))
                .filter(version -> !version.startsWith("0."))
                .toList();
        return allPatchesVersion;
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

    public boolean isVersionAfterAnOther(String version, String referentVersion) {
        String[] v1 = version.split("\\.");
        String[] v2 = referentVersion.split("\\.");

        int majorDiff = Integer.parseInt(v1[0]) - Integer.parseInt(v2[0]);
        return majorDiff != 0 ? majorDiff > 0 : Integer.parseInt(v1[1]) > Integer.parseInt(v2[1]);
    }

    public String removeFixVersion(String version) {
        return version.substring(0, version.lastIndexOf('.'));
    }

}