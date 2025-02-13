package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLPatchNomenclature;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LOLPatchNomenclatureRepository extends MongoRepository<LOLPatchNomenclature, String> {

    Optional<LOLPatchNomenclature> findFirstByPatchIdAndLanguage(String patchId, String language);

    @Aggregation(pipeline = {
            "{ $addFields: { patchParts: { $split: ['$patchId', '.'] } } }",
            "{ $addFields: { major: { $toInt: { $arrayElemAt: ['$patchParts', 0] } }, minor: { $toInt: { $arrayElemAt: ['$patchParts', 1] } } } }",
            "{ $sort: { major: -1, minor: -1 } }",
            "{ $limit: 1 }"
    })
    LOLPatchNomenclature findLatestPatch();

}
