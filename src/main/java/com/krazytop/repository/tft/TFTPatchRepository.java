package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTPatch;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TFTPatchRepository extends MongoRepository<TFTPatch, String> {

    Optional<TFTPatch> findFirstByPatchIdAndLanguage(String patchId, String language);

    @Aggregation(pipeline = {
            "{ $addFields: { patchParts: { $split: ['$patchId', '.'] } } }",
            "{ $addFields: { major: { $toInt: { $arrayElemAt: ['$patchParts', 0] } }, minor: { $toInt: { $arrayElemAt: ['$patchParts', 1] } } } }",
            "{ $sort: { major: -1, minor: -1 } }",
            "{ $limit: 1 }"
    })
    TFTPatch findLatestPatch();

}
