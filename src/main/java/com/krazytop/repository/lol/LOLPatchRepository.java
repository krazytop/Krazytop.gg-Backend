package com.krazytop.repository.lol;

import com.krazytop.nomenclature.lol.LOLPatch;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LOLPatchRepository extends MongoRepository<LOLPatch, String> {

    Optional<LOLPatch> findFirstByPatchIdAndLanguage(String patchId, String language);

    @Aggregation(pipeline = {
            "{ $addFields: { patchParts: { $split: ['$patchId', '.'] } } }",
            "{ $addFields: { major: { $toInt: { $arrayElemAt: ['$patchParts', 0] } }, minor: { $toInt: { $arrayElemAt: ['$patchParts', 1] } } } }",
            "{ $sort: { major: -1, minor: -1 } }",
            "{ $limit: 1 }"
    })
    LOLPatch findLatestPatch();

}
