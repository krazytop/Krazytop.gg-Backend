package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLAugmentNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLAugmentNomenclatureRepository extends MongoRepository<LOLAugmentNomenclature, String> {

    LOLAugmentNomenclature findFirstById(String augmentId);
}
