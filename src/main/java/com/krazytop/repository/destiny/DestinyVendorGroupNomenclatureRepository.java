package com.krazytop.repository.destiny;

import com.krazytop.nomenclature.destiny.DestinyVendorGroupNomenclature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DestinyVendorGroupNomenclatureRepository extends MongoRepository<DestinyVendorGroupNomenclature, String> {

    List<DestinyVendorGroupNomenclature> findAllByHashIn(List<Long> vendorGroupHashes);
}
