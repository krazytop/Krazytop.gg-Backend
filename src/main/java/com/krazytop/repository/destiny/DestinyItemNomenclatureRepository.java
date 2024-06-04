package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyItemNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyItemNomenclatureRepository extends MongoRepository<DestinyItemNomenclature, String> {

    List<DestinyItemNomenclature> findAllByHashIn(List<Long> itemHashes);
    DestinyItemNomenclature findByHash(Long itemHash);
}
