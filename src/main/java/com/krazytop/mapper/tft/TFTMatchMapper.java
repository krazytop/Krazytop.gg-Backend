package com.krazytop.mapper.tft;

import com.krazytop.api_gateway.model.generated.TFTMatchDTO;
import com.krazytop.entity.tft.TFTMatch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TFTMatchMapper {

    TFTMatchDTO toDTO(TFTMatch board);
}
