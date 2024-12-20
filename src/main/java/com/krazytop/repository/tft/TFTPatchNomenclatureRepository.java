package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTPatchNomenclature;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTPatchNomenclatureRepository extends MongoRepository<TFTPatchNomenclature, String> {

    TFTPatchNomenclature findFirstByPatchIdAndLanguage(String patchId, String language);

    @Aggregation(pipeline = {
            "{ $addFields: { patchParts: { $split: ['$patchId', '.'] } } }",
            "{ $addFields: { major: { $toInt: { $arrayElemAt: ['$patchParts', 0] } }, minor: { $toInt: { $arrayElemAt: ['$patchParts', 1] } } } }",
            "{ $sort: { major: -1, minor: -1 } }",
            "{ $limit: 1 }"
    })
    TFTPatchNomenclature findLatestPatch();

}
