package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyVendorNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyVendorNomenclatureRepository extends MongoRepository<DestinyVendorNomenclature, String> {

    List<DestinyVendorNomenclature> findAllByHashIn(List<Long> vendorHashes);
}
