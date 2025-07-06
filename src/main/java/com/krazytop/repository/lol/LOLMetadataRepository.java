package com.krazytop.repository.lol;

import com.krazytop.entity.lol.LOLMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LOLMetadataRepository extends MongoRepository<LOLMetadata, String> {

}
