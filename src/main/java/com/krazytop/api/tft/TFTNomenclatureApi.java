package com.krazytop.api.tft;

import com.krazytop.nomenclature.tft.*;
import com.krazytop.repository.tft.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TFTNomenclatureApi {

    private final TFTAugmentNomenclatureRepository augmentNomenclatureRepository;
    private final TFTQueueNomenclatureRepository queueNomenclatureRepository;
    private final TFTUnitNomenclatureRepository unitNomenclatureRepository;
    private final TFTItemNomenclatureRepository itemNomenclatureRepository;
    private final TFTTraitNomenclatureRepository traitNomenclatureRepository;

    @Autowired
    public TFTNomenclatureApi(TFTAugmentNomenclatureRepository augmentNomenclatureRepository, TFTQueueNomenclatureRepository queueNomenclatureRepository, TFTUnitNomenclatureRepository unitNomenclatureRepository, TFTItemNomenclatureRepository itemNomenclatureRepository, TFTTraitNomenclatureRepository traitNomenclatureRepository) {
        this.augmentNomenclatureRepository = augmentNomenclatureRepository;
        this.queueNomenclatureRepository = queueNomenclatureRepository;
        this.unitNomenclatureRepository = unitNomenclatureRepository;
        this.itemNomenclatureRepository = itemNomenclatureRepository;
        this.traitNomenclatureRepository = traitNomenclatureRepository;
    }

    public TFTAugmentNomenclature getAugmentNomenclature(String augmentId) {
        return augmentNomenclatureRepository.findFirstById(augmentId);
    }

    public TFTQueueNomenclature getQueueNomenclature(String queueId) {
        return queueNomenclatureRepository.findFirstById(queueId);
    }

    public TFTUnitNomenclature getUnitNomenclature(String unitId) {
        return unitNomenclatureRepository.findFirstById("\\b" + unitId + "\\b");
    }

    public TFTItemNomenclature getItemNomenclature(String itemId) {
        return itemNomenclatureRepository.findFirstById(itemId);
    }

    public TFTTraitNomenclature getTraitNomenclature(String traitId) {
        return traitNomenclatureRepository.findFirstById(traitId);
    }

/*
    public List<TFTRankEntity> updateRank(List<TFTRankEntity> ranks) {
        List<TFTRankEntity> ranksToUpdate = new ArrayList<>();

        for (TFTRankEntity rank : ranks) {
            rank.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            TFTRankEntity localRank = tftRankRepository.findFirstBySummonerIdOrderByUpdateDateDesc(rank.getSummonerId());

            if (localRank != null) {
                if (!rank.equals(localRank)) {
                    ranksToUpdate.add(rank);
                }
            } else {
                ranksToUpdate.add(rank);
            }
        }
        tftRankRepository.saveAll(ranksToUpdate);
        return ranks;
    }
*/
}
