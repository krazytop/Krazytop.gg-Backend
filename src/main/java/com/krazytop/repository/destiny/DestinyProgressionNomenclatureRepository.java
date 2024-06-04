package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyProgressionNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyProgressionNomenclatureRepository extends MongoRepository<DestinyProgressionNomenclature, String> {

    List<DestinyProgressionNomenclature> findAllByHashIn(List<Long> progressionHashes);
}
