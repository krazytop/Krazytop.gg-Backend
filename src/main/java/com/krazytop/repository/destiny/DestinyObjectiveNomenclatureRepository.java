package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyObjectiveNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyObjectiveNomenclatureRepository extends MongoRepository<DestinyObjectiveNomenclature, String> {

    List<DestinyObjectiveNomenclature> findAllByHashIn(List<Long> objectiveHashes);
    DestinyObjectiveNomenclature findByHash(Long objectiveHash);
}
