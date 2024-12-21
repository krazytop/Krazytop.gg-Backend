package com.krazytop.service.riot;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RIOTMetadataService {

    private final RIOTMetadataRepository metadataRepository;

    @Autowired
    public RIOTMetadataService(RIOTMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public RIOTMetadataEntity getMetadata() {
        return metadataRepository.findFirstByOrderByIdAsc().orElse(null);
    }

}
