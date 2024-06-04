package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyMetricNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyMetricNomenclatureRepository extends MongoRepository<DestinyMetricNomenclature, String> {

    List<DestinyMetricNomenclature> findAllByHashIn(List<Long> metricHashes);
    DestinyMetricNomenclature findByHash(Long metricHash);
}
