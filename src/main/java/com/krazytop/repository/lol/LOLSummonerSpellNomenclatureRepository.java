package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLSummonerSpellNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLSummonerSpellNomenclatureRepository extends MongoRepository<LOLSummonerSpellNomenclature, String> {

    LOLSummonerSpellNomenclature findFirstById(String summmonerSpellId);
}
