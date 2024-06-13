package com.krazytop.nomenclature_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.entity.destiny.DestinyIntervalObjectiveEntity;
import com.krazytop.entity.destiny.DestinyItemQuantityEntity;
import com.krazytop.entity.destiny.DestinyProgressionStepEntity;
import com.krazytop.nomenclature.destiny.*;
import com.krazytop.repository.destiny.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DestinyNomenclatureManagement {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinyNomenclatureManagement.class);

    private static final String FOLDER = "/src/main/resources/data/destiny/";

    private final DestinyClassNomenclatureRepository classNomenclatureRepository;
    private final DestinyRecordNomenclatureRepository recordNomenclatureRepository;
    private final DestinyVendorNomenclatureRepository vendorNomenclatureRepository;
    private final DestinyVendorGroupNomenclatureRepository vendorGroupNomenclatureRepository;
    private final DestinyProgressionNomenclatureRepository progressionNomenclatureRepository;
    private final DestinyItemNomenclatureRepository itemNomenclatureRepository;
    private final DestinyPresentationNodeNomenclatureRepository presentationNodeNomenclatureRepository;
    private final DestinyPresentationTreeNomenclatureRepository presentationNodeTreeNomenclatureRepository;
    private final DestinyCollectibleNomenclatureRepository collectibleNomenclatureRepository;
    private final DestinyMetricNomenclatureRepository metricNomenclatureRepository;
    private final DestinyObjectiveNomenclatureRepository objectiveNomenclatureRepository;

    @Autowired
    public DestinyNomenclatureManagement(DestinyClassNomenclatureRepository classNomenclatureRepository, DestinyRecordNomenclatureRepository recordNomenclatureRepository, DestinyVendorNomenclatureRepository vendorNomenclatureRepository, DestinyVendorGroupNomenclatureRepository vendorGroupNomenclatureRepository, DestinyProgressionNomenclatureRepository progressionNomenclatureRepository, DestinyItemNomenclatureRepository itemNomenclatureRepository, DestinyPresentationNodeNomenclatureRepository presentationNodeNomenclatureRepository, DestinyPresentationTreeNomenclatureRepository presentationNodeTreeNomenclatureRepository, DestinyCollectibleNomenclatureRepository collectibleNomenclatureRepository, DestinyMetricNomenclatureRepository metricNomenclatureRepository, DestinyObjectiveNomenclatureRepository objectiveNomenclatureRepository) {
        this.classNomenclatureRepository = classNomenclatureRepository;
        this.recordNomenclatureRepository = recordNomenclatureRepository;
        this.vendorNomenclatureRepository = vendorNomenclatureRepository;
        this.vendorGroupNomenclatureRepository = vendorGroupNomenclatureRepository;
        this.progressionNomenclatureRepository = progressionNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.presentationNodeNomenclatureRepository = presentationNodeNomenclatureRepository;
        this.presentationNodeTreeNomenclatureRepository = presentationNodeTreeNomenclatureRepository;
        this.collectibleNomenclatureRepository = collectibleNomenclatureRepository;
        this.metricNomenclatureRepository = metricNomenclatureRepository;
        this.objectiveNomenclatureRepository = objectiveNomenclatureRepository;
    }

    private String downloadJson(String url) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("https://www.bungie.net" + url);
            httpget.addHeader("X-API-Key", "043641212e7e460ca38737cbe0e93203");

            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200 && response.getEntity() != null) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    LOGGER.error("Error while retrieving Bungie manifest : {}", response);
                    throw new IOException("Failed to download manifest");
                }
            }
        }
    }

    private String getNomenclaturePath(String nomenclature) throws IOException {
        File itemFile = new File(getCurrentWorkingDirectory() + FOLDER + "manifest.json");
        Map<?, ?> jsonData = new ObjectMapper().readValue(itemFile, Map.class);
        return (String) ((Map<?, ?>) ((Map<?, ?>) ((Map<?, ?>) jsonData.get("Response")).get("jsonWorldComponentContentPaths")).get("fr")).get(nomenclature);
    }

    private void updateManifestFile(String jsonString, Path filePath) throws IOException {
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile(), false)) {
            outputStream.write(jsonString.getBytes());
            LOGGER.info("Manifest downloaded successfully to: {}", filePath);
        }

    }

    private String getManifestVersion(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> latestManifestData = objectMapper.readValue(jsonString, Map.class);
        return (String) ((Map<?, ?>) latestManifestData.get("Response")).get("version");
    }

    public boolean updateManifest() throws IOException {
        try {
            String jsonString = downloadJson("/Platform/Destiny2/Manifest/");
            Path filePath = Paths.get(getCurrentWorkingDirectory(), FOLDER, "/manifest.json");

            String latesManifestVersion = getManifestVersion(jsonString);

            String currentManifestVersion = "";
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
                latesManifestVersion = getManifestVersion(new String(Files.readAllBytes(filePath)));
            } catch (IOException e) {
                LOGGER.error("Error reading current manifest: {}", e.getMessage());
            }

            if (!currentManifestVersion.equals(latesManifestVersion)) {
                updateManifestFile(jsonString, filePath);
                LOGGER.info("Manifest updated to version: {}", latesManifestVersion);
                return true;
            } else {
                LOGGER.info("Manifest is already up-to-date. Current version: {}", currentManifestVersion);
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("Error downloading or parsing manifest: {}", e.getMessage());
            throw new IOException();
        }
    }

    public void updateClassNomenclature() throws IOException, NullPointerException {
        String nomenclaturePath = getNomenclaturePath("DestinyClassDefinition");
        String classJson = downloadJson(nomenclaturePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> classData = objectMapper.readValue(classJson, Map.class);
        classNomenclatureRepository.deleteAll();
        List<DestinyClassNomenclature> classNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : classData.entrySet()) {
            DestinyClassNomenclature classNomenclature = new DestinyClassNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            classNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            classNomenclature.setNameByGender((Map<Long, String>) entryData.get("genderedClassNamesByGenderHash"));
            classNomenclatures.add(classNomenclature);
        }
        classNomenclatureRepository.saveAll(classNomenclatures);
    }

    public void updateVendorNomenclature() throws IOException, NullPointerException {
        String nomenclaturePath = getNomenclaturePath("DestinyVendorDefinition");
        String vendorsJson = downloadJson(nomenclaturePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> vendorData = objectMapper.readValue(vendorsJson, Map.class);
        vendorNomenclatureRepository.deleteAll();
        List<DestinyVendorNomenclature> vendorNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : vendorData.entrySet()) {
            DestinyVendorNomenclature vendorNomenclature = new DestinyVendorNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            vendorNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
            vendorNomenclature.setName(displayProperties.get("name"));
            vendorNomenclature.setIcon(displayProperties.get("smallTransparentIcon"));
            vendorNomenclature.setIconBackground(displayProperties.get("icon"));
            if (vendorNomenclature.getIcon() != null && vendorNomenclature.getIconBackground() != null && !Objects.equals(vendorNomenclature.getName(), "")) {
                vendorNomenclatures.add(vendorNomenclature);
            }
        }
        vendorNomenclatureRepository.saveAll(vendorNomenclatures);
    }

    public void updateVendorGroupNomenclature() throws IOException, NullPointerException {
        String nomenclaturePath = getNomenclaturePath("DestinyVendorGroupDefinition");
        String vendorGroupsJson = downloadJson(nomenclaturePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> vendorGroupData = objectMapper.readValue(vendorGroupsJson, Map.class);
        vendorGroupNomenclatureRepository.deleteAll();
        List<DestinyVendorGroupNomenclature> vendorGroupNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : vendorGroupData.entrySet()) {
            DestinyVendorGroupNomenclature vendorGroupNomenclature = new DestinyVendorGroupNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            vendorGroupNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            vendorGroupNomenclature.setName((String)entryData.get("categoryName"));
            vendorGroupNomenclatures.add(vendorGroupNomenclature);
        }
        vendorGroupNomenclatureRepository.saveAll(vendorGroupNomenclatures);
    }

    public void updateProgressionNomenclature() throws IOException, NullPointerException {
        String nomenclaturePath = getNomenclaturePath("DestinyProgressionDefinition");
        String progrssionsJson = downloadJson(nomenclaturePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> classData = objectMapper.readValue(progrssionsJson, Map.class);
        progressionNomenclatureRepository.deleteAll();
        List<DestinyProgressionNomenclature> progressionNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : classData.entrySet()) {
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();

            List<Map<String, ?>> stepsEntry = (List<Map<String, ?>>) entryData.get("steps");
            if (stepsEntry != null && !stepsEntry.isEmpty()) {
                DestinyProgressionNomenclature progressionNomenclature = new DestinyProgressionNomenclature();
                progressionNomenclature.setHash(getHashAsLong(entryData.get("hash")));
                progressionNomenclature.setRepeatLastStep((Boolean)entryData.get("repeatLastStep"));
                Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
                progressionNomenclature.setName(displayProperties.get("name"));
                progressionNomenclature.setIcon(displayProperties.get("icon"));
                progressionNomenclature.setDescription(displayProperties.get("description"));

                List<DestinyProgressionStepEntity> steps = new ArrayList<>();
                stepsEntry.forEach(stepData -> {
                    DestinyProgressionStepEntity step = new DestinyProgressionStepEntity();
                    step.setName((String) stepData.get("stepName"));
                    step.setIcon((String) stepData.get("icon"));
                    step.setProgressTotal(getHashAsLong(stepData.get("progressTotal")));
                    steps.add(step);
                });
                progressionNomenclature.setSteps(steps);
                progressionNomenclatures.add(progressionNomenclature);
            }
        }
        progressionNomenclatureRepository.saveAll(progressionNomenclatures);
    }

    public void updateItemNomenclature() throws IOException, NullPointerException {
        String nomenclaturePath = getNomenclaturePath("DestinyInventoryItemLiteDefinition");
        String itemsJson = downloadJson(nomenclaturePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> itemData = objectMapper.readValue(itemsJson, Map.class);
        itemNomenclatureRepository.deleteAll();
        List<DestinyItemNomenclature> itemNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : itemData.entrySet()) {
            DestinyItemNomenclature itemNomenclature = new DestinyItemNomenclature();
            itemNomenclature.setHash(getHashAsLong(entry.getKey()));
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
            itemNomenclature.setName(displayProperties.get("name"));
            itemNomenclature.setDescription(displayProperties.get("description"));
            itemNomenclature.setIcon(displayProperties.get("icon"));
            itemNomenclature.setItemTypeDisplayName((String) entryData.get("itemTypeDisplayName"));
            Map<String, ?> inventory = (Map<String, ?>) entryData.get("inventory");
            itemNomenclature.setMaxStackSize(getHashAsLong(inventory.get("maxStackSize")));
            itemNomenclature.setBucketTypeHash(getHashAsLong(inventory.get("bucketTypeHash")));
            itemNomenclature.setRecoveryBucketTypeHash(getHashAsLong(inventory.get("recoveryBucketTypeHash")));
            itemNomenclature.setRecoveryBucketTypeHash(getHashAsLong(inventory.get("recoveryBucketTypeHash")));
            itemNomenclature.setTierTypeHash(getHashAsLong(inventory.get("tierTypeHash")));
            itemNomenclature.setInstanceItem((boolean)inventory.get("isInstanceItem"));
            itemNomenclature.setTierTypeName((String)inventory.get("tierTypeName"));
            Map<String, Long> equippingBlock = (Map<String, Long>) entryData.get("equippingBlock");
            if (equippingBlock != null) itemNomenclature.setEquipmentSlotTypeHash(getHashAsLong(equippingBlock.get("equipmentSlotTypeHash")));
            itemNomenclature.setSummaryItemHash(getHashAsLong(entryData.get("summaryItemHash")));
            itemNomenclature.setIconWatermark((String)entryData.get("iconWatermark"));
            itemNomenclature.setSummaryItemHash(getHashAsLong(entryData.get("summaryItemHash")));
            itemNomenclature.setNonTransferrable((boolean)entryData.get("nonTransferrable"));
            itemNomenclature.setSpecialItemType(getHashAsLong(entryData.get("specialItemType")));
            itemNomenclature.setItemType(getHashAsLong(entryData.get("itemType")));
            itemNomenclature.setItemSubType(getHashAsLong(entryData.get("itemSubType")));
            itemNomenclature.setClassType(getHashAsLong(entryData.get("classType")));
            itemNomenclature.setDefaultDamageType(getHashAsLong(entryData.get("defaultDamageType")));
            itemNomenclature.setEquippable((boolean)entryData.get("equippable"));
            itemNomenclatures.add(itemNomenclature);
        }
        itemNomenclatureRepository.saveAll(itemNomenclatures);
    }

    public void updateRecordNomenclature() throws IOException, NullPointerException {
        String recordPath = getNomenclaturePath("DestinyRecordDefinition");
        String recordJson = downloadJson(recordPath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> recordData = objectMapper.readValue(recordJson, Map.class);
        recordNomenclatureRepository.deleteAll();
        List<DestinyRecordNomenclature> recordNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : recordData.entrySet()) {
            DestinyRecordNomenclature recordNomenclature = new DestinyRecordNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            recordNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
            recordNomenclature.setName(displayProperties.get("name"));
            recordNomenclature.setDescription(displayProperties.get("description"));
            recordNomenclature.setIcon(displayProperties.get("icon"));
            recordNomenclature.setRecordTypeName((String)entryData.get("recordTypeName"));
            Map<String, ?> expirationInfo = (Map<String, ?>) entryData.get("expirationInfo");
            if (expirationInfo != null) {
                recordNomenclature.setHasExpiration((boolean) expirationInfo.get("hasExpiration"));
                recordNomenclature.setExpirationDescription((String) expirationInfo.get("expirationDescription"));
            }
            recordNomenclature.setObjectives(CollectionUtils.emptyIfNull((List<?>)entryData.get("objectiveHashes")).stream().map(objectiveHash -> objectiveNomenclatureRepository.findByHash(getHashAsLong(objectiveHash))).toList());
            Map<String,List<Map<String, ?>>> intervalInfo = (Map<String, List<Map<String, ?>>>) entryData.get("intervalInfo");
            if (intervalInfo != null) {
                List<Map<String, ?>> intervalObjectives = intervalInfo.get("intervalObjectives");
                List<Map<String, ?>> intervalRewardItems = intervalInfo.get("intervalRewards");
                int intervalsCount = intervalObjectives.size();
                List<DestinyIntervalObjectiveEntity> intervalObjectiveEntities = new ArrayList<>();
                for (int interval = 0; interval < intervalsCount; interval++) {
                    DestinyIntervalObjectiveEntity intervalObjective = new DestinyIntervalObjectiveEntity();
                    intervalObjective.setRewardItems(((Map<String, List<Map<String,?>>>)intervalRewardItems.get(interval)).get("intervalRewardItems").stream().map(rewardItem -> {
                        DestinyItemQuantityEntity itemQuantity = new DestinyItemQuantityEntity();
                        itemQuantity.setItem(itemNomenclatureRepository.findByHash(getHashAsLong(rewardItem.get("itemHash"))));
                        itemQuantity.setQuantity(getHashAsLong(rewardItem.get("quantity")));
                        return itemQuantity;
                    }).toList());
                    intervalObjective.setObjective(objectiveNomenclatureRepository.findByHash(getHashAsLong(intervalObjectives.get(interval).get("intervalObjectiveHash"))));
                    intervalObjective.setScore(getHashAsLong(intervalObjectives.get(interval).get("intervalScoreValue")));
                    intervalObjectiveEntities.add(intervalObjective);
                }
                recordNomenclature.setIntervalObjectives(intervalObjectiveEntities);
            }
            Map<String, ?> titleInfo = (Map<String, ?>) entryData.get("titleInfo");
            if (titleInfo != null && (boolean) titleInfo.get("hasTitle")) {
                recordNomenclature.setTitlesByGender((Map<Long, String>) titleInfo.get("titlesByGenderHash"));
            }
            recordNomenclature.setRewardItems(CollectionUtils.emptyIfNull((List<Map<String,?>>) entryData.get("rewardItems")).stream().map(rewardItem -> {
                DestinyItemQuantityEntity itemQuantity = new DestinyItemQuantityEntity();
                itemQuantity.setItem(itemNomenclatureRepository.findByHash(getHashAsLong(rewardItem.get("itemHash"))));
                itemQuantity.setQuantity(getHashAsLong(rewardItem.get("quantity")));
                return itemQuantity;
            }).toList());
            recordNomenclatures.add(recordNomenclature);
        }
        recordNomenclatureRepository.saveAll(recordNomenclatures);
    }

    public void updateObjectiveNomenclature() throws IOException, NullPointerException {
        String objectivePath = getNomenclaturePath("DestinyObjectiveDefinition");
        String objectiveJson = downloadJson(objectivePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> objectiveData = objectMapper.readValue(objectiveJson, Map.class);
        objectiveNomenclatureRepository.deleteAll();
        List<DestinyObjectiveNomenclature> objectiveNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : objectiveData.entrySet()) {
            DestinyObjectiveNomenclature objectiveNomenclature = new DestinyObjectiveNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            objectiveNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
            objectiveNomenclature.setName(displayProperties.get("name"));
            objectiveNomenclature.setDescription(displayProperties.get("description"));
            objectiveNomenclature.setIcon(displayProperties.get("icon"));
            objectiveNomenclature.setCompletionValue(getHashAsLong(entryData.get("completionValue")));
            objectiveNomenclature.setProgressDescription((String) entryData.get("progressDescription"));
            objectiveNomenclature.setScope(getHashAsLong(entryData.get("scope"))); //TODO location
            objectiveNomenclature.setAllowValueChangeWhenCompleted((boolean) entryData.get("allowValueChangeWhenCompleted"));
            objectiveNomenclature.setCountingDownward((boolean) entryData.get("isCountingDownward"));
            objectiveNomenclature.setAllowNegativeValue((boolean) entryData.get("allowNegativeValue"));
            objectiveNomenclature.setAllowValueChangeWhenCompleted((boolean) entryData.get("allowValueChangeWhenCompleted"));
            objectiveNomenclature.setAllowOvercompletion((boolean) entryData.get("allowOvercompletion"));
            objectiveNomenclature.setDisplayOnlyObjective((boolean) entryData.get("isDisplayOnlyObjective"));
            objectiveNomenclatures.add(objectiveNomenclature);
        }
        objectiveNomenclatureRepository.saveAll(objectiveNomenclatures);
    }

    public void updateCollectibleNomenclature() throws IOException, NullPointerException {
        String collectiblePath = getNomenclaturePath("DestinyCollectibleDefinition");
        String collectibleJson = downloadJson(collectiblePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> collectibleData = objectMapper.readValue(collectibleJson, Map.class);
        collectibleNomenclatureRepository.deleteAll();
        List<DestinyCollectibleNomenclature> collectibleNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : collectibleData.entrySet()) {
            DestinyCollectibleNomenclature collectibleNomenclature = new DestinyCollectibleNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            collectibleNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            collectibleNomenclature.setSourceString((String) entryData.get("sourceString"));
            collectibleNomenclature.setSourceHash(getHashAsLong(entryData.get("sourceHash")));
            collectibleNomenclature.setItemNomenclature(itemNomenclatureRepository.findByHash(getHashAsLong(entryData.get("itemHash"))));
            collectibleNomenclature.setNodeType(getHashAsLong(entryData.get("presentationNodeType")));
            collectibleNomenclatures.add(collectibleNomenclature);
        }
        collectibleNomenclatureRepository.saveAll(collectibleNomenclatures);
    }

    public void updateMetricNomenclature() throws IOException, NullPointerException {
        String metricPath = getNomenclaturePath("DestinyMetricDefinition");
        String metricJson = downloadJson(metricPath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> metricData = objectMapper.readValue(metricJson, Map.class);
        metricNomenclatureRepository.deleteAll();
        List<DestinyMetricNomenclature> metricNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : metricData.entrySet()) {
            DestinyMetricNomenclature metricNomenclature = new DestinyMetricNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            metricNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
            metricNomenclature.setName(displayProperties.get("name"));
            metricNomenclature.setDescription(displayProperties.get("description"));
            metricNomenclature.setIcon(displayProperties.get("icon"));
            metricNomenclature.setNodeType(getHashAsLong(entryData.get("presentationNodeType")));
            metricNomenclature.setTrackingObjective(objectiveNomenclatureRepository.findByHash(getHashAsLong(entryData.get("trackingObjectiveHash"))));
            metricNomenclature.setTraitHashes(CollectionUtils.emptyIfNull((List<?>) entryData.get("traitHashes")).stream().map(this::getHashAsLong).toList());
            metricNomenclatures.add(metricNomenclature);
        }
        metricNomenclatureRepository.saveAll(metricNomenclatures);
    }

    public void updatePresentationNodeNomenclature() throws IOException, NullPointerException {
        String presentationNodePath = getNomenclaturePath("DestinyPresentationNodeDefinition");
        String presentationNodeJson = downloadJson(presentationNodePath);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?,?> presentationNodeData = objectMapper.readValue(presentationNodeJson, Map.class);
        List<DestinyPresentationNodeNomenclature> presentationNodes = new ArrayList<>();
        presentationNodeNomenclatureRepository.deleteAll();
        List<DestinyPresentationNodeNomenclature> presentationNodeNomenclatures = new ArrayList<>();
        for (Map.Entry<?,?> entry : presentationNodeData.entrySet()) {
            DestinyPresentationNodeNomenclature presentationNodeNomenclature = new DestinyPresentationNodeNomenclature();
            Map<?, ?> entryData = (Map<?, ?>) entry.getValue();
            presentationNodeNomenclature.setHash(getHashAsLong(entryData.get("hash")));
            Map<String, String> displayProperties = (Map<String, String>) entryData.get("displayProperties");
            presentationNodeNomenclature.setName(displayProperties.get("name"));
            presentationNodeNomenclature.setDescription(displayProperties.get("description"));
            presentationNodeNomenclature.setIcon(displayProperties.get("icon"));
            presentationNodeNomenclature.setNodeType(getHashAsLong(entryData.get("nodeType")));
            presentationNodeNomenclature.setSeasonal((boolean) entryData.get("isSeasonal"));
            presentationNodeNomenclature.setObjective(objectiveNomenclatureRepository.findByHash(getHashAsLong(entryData.get("objectiveHash"))));
            presentationNodeNomenclature.setParentNodeHashes(CollectionUtils.emptyIfNull((List<?>) entryData.get("parentNodeHashes")).stream().map(this::getHashAsLong).toList());
            presentationNodeNomenclature.setChildrenNodeHash(getChildrenHashList(entryData, "presentationNodes", "presentationNodeHash"));
            presentationNodeNomenclature.setChildrenCollectible(collectibleNomenclatureRepository.findAllByHashIn(getChildrenHashList(entryData, "collectibles", "collectibleHash")));
            presentationNodeNomenclature.setChildrenRecord(recordNomenclatureRepository.findAllByHashIn(getChildrenHashList(entryData, "records", "recordHash")));
            presentationNodeNomenclature.setChildrenMetric(metricNomenclatureRepository.findAllByHashIn(getChildrenHashList(entryData, "metrics", "metricHash")));
            presentationNodeNomenclature.setChildrenCraftable(itemNomenclatureRepository.findAllByHashIn(getChildrenHashList(entryData, "craftables", "craftableItemHash")));
            presentationNodes.add(presentationNodeNomenclature);
            presentationNodeNomenclatures.add(presentationNodeNomenclature);
        }
        presentationNodeNomenclatureRepository.saveAll(presentationNodeNomenclatures);
        updatePresentationNodeTreeNomenclature(presentationNodes);
    }

    private void updatePresentationNodeTreeNomenclature(List<DestinyPresentationNodeNomenclature> presentationNodes) { //TODO voir si je ne le fait pas seulement pour des hashs particuliers
        presentationNodes.forEach( presentationNode -> {
           if (presentationNode.getParentNodeHashes().isEmpty()){
                DestinyPresentationTreeNomenclature tree = buildPresentationNodeTreeNomenclature(presentationNode.getHash());
                presentationNodeTreeNomenclatureRepository.save(tree);
            }
        });
    }

    private DestinyPresentationTreeNomenclature buildPresentationNodeTreeNomenclature(Long nodeHash) {
        DestinyPresentationNodeNomenclature node = presentationNodeNomenclatureRepository.findByHash(nodeHash);
        DestinyPresentationTreeNomenclature tree = new DestinyPresentationTreeNomenclature();
        tree.setHash(node.getHash());
        tree.setName(node.getName());
        tree.setNodeType(node.getNodeType());
        tree.setSeasonal(node.isSeasonal());
        tree.setIcon(node.getIcon());
        tree.setDescription(node.getDescription());
        tree.setChildrenNode(node.getChildrenNodeHash().stream().map(this::buildPresentationNodeTreeNomenclature).toList());
        tree.setChildrenCollectible(node.getChildrenCollectible());
        tree.setChildrenCraftable(node.getChildrenCraftable());
        tree.setChildrenRecord(node.getChildrenRecord());
        tree.setChildrenMetric(node.getChildrenMetric());
        tree.setObjective(node.getObjective());
        return tree;
    }



    private List<Long> getChildrenHashList(Map<?, ?> entryData, String childrenNode, String childrenNodeHash) {
        Map<String, ?> children = MapUtils.emptyIfNull((Map<String, ?>) entryData.get("children"));
        return CollectionUtils.emptyIfNull((List<Map<String,Long>>) children.get(childrenNode)).stream()
                .map(presentationNode -> getHashAsLong(presentationNode.get(childrenNodeHash)))
                .toList();
    }

    private Long getHashAsLong(Object hash) {
        if (hash == null) return null;
        else if (hash instanceof Long) return (Long) hash;
        else if (hash instanceof Integer) return Long.valueOf((Integer) hash);
        else if (hash instanceof String) return Long.valueOf((String) hash);
        else throw new RuntimeException("Function getHashAsLong can't be processed, hash is not a compatible type");
    }

    private String getCurrentWorkingDirectory() {
        return System.getProperty("user.dir");
    }
}
