package com.krazytop.mapper.tft;

import com.krazytop.api_gateway.model.generated.TFTPatchDTO;
import com.krazytop.nomenclature.tft.TFTPatch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TFTPatchMapper {

    TFTPatchDTO toDTO(TFTPatch patch);
    TFTPatch toEntity(TFTPatchDTO metadataDTO);
}
