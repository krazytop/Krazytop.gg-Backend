package com.krazytop.repository.tft;

import com.krazytop.entity.tft.TFTMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TFTMetadataRepository extends MongoRepository<TFTMetadata, String> {

    Optional<TFTMetadata> findFirstByOrderByIdAsc();
}
