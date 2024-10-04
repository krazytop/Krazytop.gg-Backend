package com.krazytop.repository.clash_royal;

import com.krazytop.nomenclature.clash_royal.CRArenaNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CRArenaNomenclatureRepository extends MongoRepository<CRArenaNomenclature, String> {

    CRArenaNomenclature findFirstById(int arenaId);
}
