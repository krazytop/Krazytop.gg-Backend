package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyCollectibleNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyCollectibleNomenclatureRepository extends MongoRepository<DestinyCollectibleNomenclature, String> {

    List<DestinyCollectibleNomenclature> findAllByHashIn(List<Long> collectibleHashes);
    DestinyCollectibleNomenclature findByHash(Long collectibleHash);
}
