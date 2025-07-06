package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.RIOTMetadataDTO;
import com.krazytop.entity.lol.LOLMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LOLMetadataMapper {//TODO creer un RIOTMetadata et le passer au micro service lol. Supprimer le LOLMetadata

    @Mapping(source = "currentSeason", target = "currentSeasonOrSet")
    RIOTMetadataDTO toDTO(LOLMetadata metadata);
}
