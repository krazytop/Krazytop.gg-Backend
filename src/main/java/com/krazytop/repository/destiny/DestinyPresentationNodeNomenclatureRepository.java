package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyPresentationNodeNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyPresentationNodeNomenclatureRepository extends MongoRepository<DestinyPresentationNodeNomenclature, String> {

    List<DestinyPresentationNodeNomenclature> findAllByHashIn(List<Long> presentationNodeHashes);
    DestinyPresentationNodeNomenclature findByHash(Long presentationNodeHash);
}
