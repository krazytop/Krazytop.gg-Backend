package com.krazytop.service.destiny;

import com.krazytop.nomenclature.destiny.*;
import com.krazytop.repository.destiny.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DestinyNomenclatureService {

    private final DestinyClassNomenclatureRepository classNomenclatureRepository;
    private final DestinyVendorNomenclatureRepository vendorNomenclatureRepository;
    private final DestinyVendorGroupNomenclatureRepository vendorGroupNomenclatureRepository;
    private final DestinyProgressionNomenclatureRepository progressionNomenclatureRepository;
    private final DestinyItemNomenclatureRepository itemNomenclatureRepository;
    private final DestinyPresentationTreeNomenclatureRepository presentationTreeNomenclatureRepository;
    private final DestinyRecordNomenclatureRepository recordNomenclatureRepository;

    @Autowired
    public DestinyNomenclatureService(DestinyClassNomenclatureRepository classNomenclatureRepository, DestinyVendorNomenclatureRepository vendorNomenclatureRepository, DestinyVendorGroupNomenclatureRepository vendorGroupNomenclatureRepository, DestinyProgressionNomenclatureRepository progressionNomenclatureRepository, DestinyItemNomenclatureRepository itemNomenclatureRepository, DestinyPresentationTreeNomenclatureRepository presentationTreeNomenclatureRepository, DestinyRecordNomenclatureRepository recordNomenclatureRepository) {
        this.classNomenclatureRepository = classNomenclatureRepository;
        this.vendorNomenclatureRepository = vendorNomenclatureRepository;
        this.vendorGroupNomenclatureRepository = vendorGroupNomenclatureRepository;
        this.progressionNomenclatureRepository = progressionNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.presentationTreeNomenclatureRepository = presentationTreeNomenclatureRepository;
        this.recordNomenclatureRepository = recordNomenclatureRepository;
    }

    public Map<Long, DestinyClassNomenclature> getClassNomenclatures(List<Long> classHashList) {
        List<DestinyClassNomenclature> classNomenclatures = classNomenclatureRepository.findAllByHashIn(classHashList);
        Map<Long, DestinyClassNomenclature> classNomenclatureMap = new HashMap<>();
        for (DestinyClassNomenclature classNomenclature : classNomenclatures) {
            classNomenclatureMap.put(classNomenclature.getHash(), classNomenclature);
        }
        return classNomenclatureMap;
    }

    public Map<Long, DestinyVendorNomenclature> getVendorNomenclatures(List<Long> vendorHashList) {
        List<DestinyVendorNomenclature> vendorNomenclatures = vendorNomenclatureRepository.findAllByHashIn(vendorHashList);
        Map<Long, DestinyVendorNomenclature> vendorNomenclaturesMap = new HashMap<>();
        for (DestinyVendorNomenclature vendorNomenclature : vendorNomenclatures) {
            vendorNomenclaturesMap.put(vendorNomenclature.getHash(), vendorNomenclature);
        }
        return vendorNomenclaturesMap;
    }

    public Map<Long, DestinyVendorGroupNomenclature> getVendorGroupNomenclatures(List<Long> vendorGroupHashList) {
        List<DestinyVendorGroupNomenclature> vendorGroupNomenclatures = vendorGroupNomenclatureRepository.findAllByHashIn(vendorGroupHashList);
        Map<Long, DestinyVendorGroupNomenclature> vendorGroupNomenclaturesMap = new HashMap<>();
        for (DestinyVendorGroupNomenclature vendorGroupNomenclature : vendorGroupNomenclatures) {
            vendorGroupNomenclaturesMap.put(vendorGroupNomenclature.getHash(), vendorGroupNomenclature);
        }
        return vendorGroupNomenclaturesMap;
    }

    public Map<Long, DestinyProgressionNomenclature> getProgressionNomenclatures(List<Long> progressionHashList) {
        List<DestinyProgressionNomenclature> progressionNomenclatures = progressionNomenclatureRepository.findAllByHashIn(progressionHashList);
        Map<Long, DestinyProgressionNomenclature> progressionNomenclaturesMap = new HashMap<>();
        for (DestinyProgressionNomenclature progressionNomenclature : progressionNomenclatures) {
            progressionNomenclaturesMap.put(progressionNomenclature.getHash(), progressionNomenclature);
        }
        return progressionNomenclaturesMap;
    }

    public Map<Long, DestinyItemNomenclature> getItemNomenclatures(List<Long> itemHashList) {
        List<DestinyItemNomenclature> itemNomenclatures = itemNomenclatureRepository.findAllByHashIn(itemHashList);
        Map<Long, DestinyItemNomenclature> itemNomenclaturesMap = new HashMap<>();
        for (DestinyItemNomenclature itemNomenclature : itemNomenclatures) {
            itemNomenclaturesMap.put(itemNomenclature.getHash(), itemNomenclature);
        }
        return itemNomenclaturesMap;
    }

    public Map<Long, DestinyRecordNomenclature> getRecordNomenclatures(List<Long> recordHashList) {
        List<DestinyRecordNomenclature> recordNomenclatures = recordNomenclatureRepository.findAllByHashIn(recordHashList);
        Map<Long, DestinyRecordNomenclature> itemNomenclaturesMap = new HashMap<>();
        for (DestinyRecordNomenclature recordNomenclature : recordNomenclatures) {
            itemNomenclaturesMap.put(recordNomenclature.getHash(), recordNomenclature);
        }
        return itemNomenclaturesMap;
    }

    public Map<Long, DestinyPresentationTreeNomenclature> getPresentationTreeNomenclatures(List<Long> presentationTreeHashList) {
        List<DestinyPresentationTreeNomenclature> presentationTreeNomenclatures = presentationTreeNomenclatureRepository.findAllByHashIn(presentationTreeHashList);
        Map<Long, DestinyPresentationTreeNomenclature> presentationTreeNomenclaturesMap = new HashMap<>();
        for (DestinyPresentationTreeNomenclature presentationTreeNomenclature : presentationTreeNomenclatures) {
            presentationTreeNomenclaturesMap.put(presentationTreeNomenclature.getHash(), presentationTreeNomenclature);
        }
        return presentationTreeNomenclaturesMap;
    }

}
