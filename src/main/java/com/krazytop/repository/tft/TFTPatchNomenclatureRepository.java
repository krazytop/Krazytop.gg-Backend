package com.krazytop.repository.tft;

import com.krazytop.nomenclature.tft.TFTPatchNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TFTPatchNomenclatureRepository extends MongoRepository<TFTPatchNomenclature, String> {

    TFTPatchNomenclature findFirstByPatchIdAndLanguage(String patchId, String language);

}
