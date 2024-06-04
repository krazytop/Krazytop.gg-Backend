package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyPresentationTreeNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyPresentationTreeNomenclatureRepository extends MongoRepository<DestinyPresentationTreeNomenclature, String> {

    List<DestinyPresentationTreeNomenclature> findAllByHashIn(List<Long> presentationNodeTreeHashes);
}
