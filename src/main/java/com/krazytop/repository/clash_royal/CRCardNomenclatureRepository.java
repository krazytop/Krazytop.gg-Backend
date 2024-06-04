package com.krazytop.repository.clash_royal;

import com.krazytop.nomenclature.clash_royal.CRCardNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRCardNomenclatureRepository extends MongoRepository<CRCardNomenclature, String> {

    CRCardNomenclature findFirstById(int cardId);
}
