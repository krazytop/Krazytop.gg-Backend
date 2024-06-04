package com.krazytop.repository.clash_royal;

import com.krazytop.nomenclature.clash_royal.CRAccountLevelNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRAccountLevelNomenclatureRepository extends MongoRepository<CRAccountLevelNomenclature, String> {

    CRAccountLevelNomenclature findFirstByLevel(int level);
}
