package com.krazytop.repository.riot;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RIOTMetadataRepository extends MongoRepository<RIOTMetadataEntity, String> {

    Optional<RIOTMetadataEntity> findFirstByOrderByIdAsc();
}
