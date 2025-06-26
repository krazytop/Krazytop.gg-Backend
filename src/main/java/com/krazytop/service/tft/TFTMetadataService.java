package com.krazytop.service.tft;

import com.krazytop.api_gateway.model.generated.RIOTMetadataDTO;
import com.krazytop.entity.tft.TFTMetadata;
import com.krazytop.exception.CustomException;
import com.krazytop.http_responses.ApiErrorEnum;
import com.krazytop.mapper.tft.TFTMetadataMapper;
import com.krazytop.repository.tft.TFTMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TFTMetadataService {

    private final TFTMetadataRepository metadataRepository;
    private final TFTMetadataMapper metadataMapper;

    @Autowired
    public TFTMetadataService(TFTMetadataRepository metadataRepository, TFTMetadataMapper  metadataMapper) {
        this.metadataRepository = metadataRepository;
        this.metadataMapper = metadataMapper;
    }

    public RIOTMetadataDTO getMetadataDTO() {
        return getMetadata()
                .map(this.metadataMapper::toDTO)
                .orElseThrow(() -> new CustomException(ApiErrorEnum.METADATA_NOT_FOUND));
    }

    public Optional<TFTMetadata> getMetadata() {
        return metadataRepository.findAll().stream().findFirst();
    }

    public void saveMetadata(TFTMetadata metadata) {
        metadataRepository.save(metadata);
    }

}
