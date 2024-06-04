package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLRuneNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLRuneNomenclatureRepository extends MongoRepository<LOLRuneNomenclature, String> {

    LOLRuneNomenclature findFirstById(String runeId);
}
