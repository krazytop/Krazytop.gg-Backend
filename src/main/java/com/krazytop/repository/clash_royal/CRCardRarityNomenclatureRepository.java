package com.krazytop.repository.clash_royal;

import com.krazytop.nomenclature.clash_royal.CRCardRarityNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRCardRarityNomenclatureRepository extends MongoRepository<CRCardRarityNomenclature, String> {

    CRCardRarityNomenclature findFirstByName(String rarity);
}
