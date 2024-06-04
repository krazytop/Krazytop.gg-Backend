package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLItemNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLItemNomenclatureRepository extends MongoRepository<LOLItemNomenclature, String> {

    LOLItemNomenclature findFirstById(String itemId);
}
