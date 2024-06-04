package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyClassNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyClassNomenclatureRepository extends MongoRepository<DestinyClassNomenclature, String> {

    List<DestinyClassNomenclature> findAllByHashIn(List<Long> classHashes);
}
