package com.krazytop.service.riot;

import com.krazytop.entity.riot.RIOTMetadataEntity;
import com.krazytop.repository.riot.RIOTMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

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

    public void updateMetadata(Consumer<RIOTMetadataEntity> setter) {
        RIOTMetadataEntity metadata = metadataRepository.findFirstByOrderByIdAsc().orElse(new RIOTMetadataEntity());
        setter.accept(metadata);
        metadataRepository.save(metadata);
    }

}
