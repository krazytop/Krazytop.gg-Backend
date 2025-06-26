package com.krazytop.mapper.tft;

import com.krazytop.api_gateway.model.generated.TFTPatchDTO;
import com.krazytop.nomenclature.tft.TFTPatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TFTPatchMapper {

    TFTPatchDTO toDTO(TFTPatch patch);
    TFTPatch toEntity(TFTPatchDTO metadataDTO);
}
