package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLChampionNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLChampionNomenclatureRepository extends MongoRepository<LOLChampionNomenclature, String> {

    LOLChampionNomenclature findFirstById(String championId);
}
