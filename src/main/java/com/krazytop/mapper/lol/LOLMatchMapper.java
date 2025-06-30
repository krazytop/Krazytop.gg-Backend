package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.LOLMatchDTO;
import com.krazytop.entity.lol.LOLMatch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LOLMatchMapper {

    LOLMatchDTO toDTO(LOLMatch board);
}
