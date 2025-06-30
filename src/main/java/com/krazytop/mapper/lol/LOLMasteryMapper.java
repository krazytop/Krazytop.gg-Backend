package com.krazytop.mapper.lol;

import com.krazytop.api_gateway.model.generated.LOLMasteriesDTO;
import com.krazytop.entity.lol.LOLMasteries;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LOLMasteryMapper {

    LOLMasteriesDTO toDTO(LOLMasteries board);
}
