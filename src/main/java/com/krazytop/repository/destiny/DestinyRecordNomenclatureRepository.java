package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyRecordNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyRecordNomenclatureRepository extends MongoRepository<DestinyRecordNomenclature, String> {

    DestinyRecordNomenclature findByHash(Long recordHash);
    List<DestinyRecordNomenclature> findAllByHashIn(List<Long> recordHashes);
}
