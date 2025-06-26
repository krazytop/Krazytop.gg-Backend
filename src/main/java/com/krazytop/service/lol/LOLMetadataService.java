package com.krazytop.service.lol;

import com.krazytop.api_gateway.model.generated.RIOTMetadataDTO;
import com.krazytop.entity.lol.LOLMetadata;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.mapper.lol.LOLMetadataMapper;
import com.krazytop.repository.lol.LOLMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LOLMetadataService {

    private final LOLMetadataRepository metadataRepository;
    private final LOLMetadataMapper metadataMapper;

    @Autowired
    public LOLMetadataService(LOLMetadataRepository metadataRepository, LOLMetadataMapper  metadataMapper) {
        this.metadataRepository = metadataRepository;
        this.metadataMapper = metadataMapper;
    }

    public RIOTMetadataDTO getMetadataDTO() {
        return getMetadata()
                .map(this.metadataMapper::toDTO)
                .orElseThrow(() -> new CustomException(ApiErrorEnum.METADATA_NOT_FOUND));
    }

    public Optional<LOLMetadata> getMetadata() {
        return metadataRepository.findAll().stream().findFirst();
    }

    public void saveMetadata(LOLMetadata metadata) {
        metadataRepository.save(metadata);
    }

}
