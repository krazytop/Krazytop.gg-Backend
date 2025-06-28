package com.krazytop.mapper.tft;

import com.krazytop.api_gateway.model.generated.RIOTMetadataDTO;
import com.krazytop.entity.tft.TFTMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TFTMetadataMapper {//TODO creer un RIOTMetadata et le passer au micro service tft. Supprimer le TFTMetadata

    @Mapping(source = "currentSet", target = "currentSeasonOrSet")
    RIOTMetadataDTO toDTO(TFTMetadata metadata);
}
